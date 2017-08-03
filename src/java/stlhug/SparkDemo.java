package stlhug;

import java.util.Arrays;
import org.apache.spark.*;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.*;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.*;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.twitter.TwitterUtils;

import ch.hsr.geohash.GeoHash;
import scala.Tuple2;
import twitter4j.Status;

public class SparkDemo {

	private static final class SaveAsPlaces implements VoidFunction2<JavaPairRDD<String, Long>, Time> {
		private static final long serialVersionUID = 7928785928497688684L;
		private final SparkSession spark;

		private SaveAsPlaces(SparkSession spark) {
			this.spark = spark;
		}

		@Override
		public void call(JavaPairRDD<String, Long> geoHashCounts, Time period) throws Exception {
			Dataset<Row> df = spark.createDataset(JavaPairRDD.toRDD(geoHashCounts), Encoders.tuple(Encoders.STRING(),Encoders.LONG())).toDF("geoHash","count");
			df.createOrReplaceTempView("places");
		}
	}

	public static final void main(String[] args) {

		if (args.length < 4) {
			System.err.println("Usage: JavaTwitterHashTagJoinSentiments <consumer key>"
					+ " <consumer secret> <access token> <access token secret> [<filters>]");
			System.exit(1);
		}

		String consumerKey = args[0];
		String consumerSecret = args[1];
		String accessToken = args[2];
		String accessTokenSecret = args[3];
		String[] filters = Arrays.copyOfRange(args, 4, args.length);

		// Set the system properties so that Twitter4j library used by Twitter stream
		// can use them to generate OAuth credentials
		System.setProperty("twitter4j.oauth.consumerKey", consumerKey);
		System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret);
		System.setProperty("twitter4j.oauth.accessToken", accessToken);
		System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret);

		// Create a local StreamingContext with two working thread and batch interval of
		// 60 second
		SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("SparkDemo");
		JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(60));
		
		SparkSession spark = SparkSession
				  .builder()
				  .appName("Java Spark SQL basic example")
				  .getOrCreate();

		// Get Data
		JavaReceiverInputDStream<Status> stream = TwitterUtils.createStream(jssc, filters);

		// Process Data
		JavaDStream<Status> filteredStream = stream.filter(new FilterNullGeoLocation());

		JavaPairDStream<String, Long> geoHashCounts = filteredStream
				.mapToPair(new StatusToGeoHashCountPair());

		// Act on the Data
		geoHashCounts = geoHashCounts.reduceByKey(new CombineCounts());
		
		geoHashCounts.print();
		
		geoHashCounts.foreachRDD(new SaveAsPlaces(spark));
		
		jssc.start();
		try {
			jssc.awaitTermination();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

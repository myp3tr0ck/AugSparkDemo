package stlhug;

import org.apache.spark.api.java.function.PairFunction;

import ch.hsr.geohash.GeoHash;
import scala.Tuple2;
import twitter4j.Status;

public class StatusToGeoHashCountPair implements PairFunction<Status, String, Long> {
	private static final long serialVersionUID = -3770059775514850217L;

	@Override
	public Tuple2<String, Long> call(Status status) throws Exception {
		return new Tuple2<String, Long>(GeoHash.geoHashStringWithCharacterPrecision(
					status.getGeoLocation().getLatitude(), 
					status.getGeoLocation().getLongitude(), 
					5), 
				1L);
	}
}

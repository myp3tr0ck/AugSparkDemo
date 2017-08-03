package stlhug;

import org.apache.spark.api.java.function.Function;

import twitter4j.Status;

public class FilterNullGeoLocation implements Function<Status, Boolean> {
	private static final long serialVersionUID = -7855059078579605623L;

	@Override
	public Boolean call(Status status) throws Exception {
		return status.getGeoLocation() != null;
	}
}

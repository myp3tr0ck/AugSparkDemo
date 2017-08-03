package stlhug;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import scala.Tuple2;
import twitter4j.GeoLocation;
import twitter4j.Status;

public class StatusToGeoHashCountPairTest {

	@Test
	public void getsExpectedResult() throws Exception {
		Status status = mock(Status.class);
		when(status.getGeoLocation()).thenReturn(new GeoLocation(10, -20));
		
		Tuple2<String, Long> tuple = new StatusToGeoHashCountPair().call(status);
		assertEquals("e9cbb", tuple._1());
		assertEquals(new Long(1L), tuple._2());
	}
	
}

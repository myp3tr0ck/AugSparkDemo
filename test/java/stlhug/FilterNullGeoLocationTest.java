package stlhug;

import org.junit.Test;
import org.mockito.Mock;

import twitter4j.GeoLocation;
import twitter4j.Status;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class FilterNullGeoLocationTest {

	@Test
	public void nullEqualsFalse() throws Exception {
		Status status = mock(Status.class);
		when(status.getGeoLocation()).thenReturn(null);
		
		assertFalse(new FilterNullGeoLocation().call(status));
	}
	
	@Test
	public void notNullEqualsTrue() throws Exception {
		Status status = mock(Status.class);
		when(status.getGeoLocation()).thenReturn(new GeoLocation(0, 0));
		
		assertTrue(new FilterNullGeoLocation().call(status));
	}
}

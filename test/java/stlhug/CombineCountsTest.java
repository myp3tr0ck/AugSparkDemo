package stlhug;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CombineCountsTest {

	@Test
	public void testCounts() throws Exception {
		assertEquals(new Long(3L), new CombineCounts().call(1L, 2L));
	}
	
}

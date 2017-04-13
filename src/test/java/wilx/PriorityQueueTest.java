package wilx;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.Comparator;

import org.testng.annotations.Test;

/**
 * Created by wilx on 13.4.17.
 */
public class PriorityQueueTest {

	@Test
	public void test() throws Exception {
		PriorityQueue<Double> pq = new PriorityQueue<>(Arrays.asList(1d, 2.1, 3d));
		assertEquals(pq.size(), 3);
		double prev = pq.get();
		while (!pq.isEmpty()) {
			double item = pq.get();
			assertTrue(prev > item);
			prev = item;
		}
	}

    @Test
    public void testArray() throws Exception {
        PriorityQueue<Double> pq = new PriorityQueue<>(new Double[]{1d, 2.1, 3d});
        assertEquals(pq.size(), 3);
        double prev = pq.get();
        while (!pq.isEmpty()) {
            double item = pq.get();
            assertTrue(prev > item);
            prev = item;
        }
    }

	@Test
	public void testCustomComparator() {
		PriorityQueue<Double> pq = new PriorityQueue<>(Arrays.asList(1d, 2.1, 3d), Comparator.reverseOrder());
		double prev = pq.get();
		while (!pq.isEmpty()) {
			double item = pq.get();
			assertTrue(prev < item);
			prev = item;
		}
	}
}

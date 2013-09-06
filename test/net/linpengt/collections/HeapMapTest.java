package net.linpengt.collections;

import static org.junit.Assert.*;

import org.junit.Test;

public class HeapMapTest {
	
	@Test
	public void testHeapMap() {
		int num = 20;
		System.out.println("** Test Put **");
		HeapMap<String, Double> map = new HeapMap<String, Double>();
		for (int i=num-1; i>=0; --i) {
			map.put(String.format("K%d", i), new Double(i));
			System.out.println(map);
			assertTrue(map.checkState());
		}
		
		System.out.println("** Test Put -Inf **");
		
		for (int i=num-1; i>=0; --i) {
			map.put(String.format("I%d", i), -Double.POSITIVE_INFINITY );
			System.out.println(map);
			assertTrue(map.checkState());
		}

		System.out.println("** Test Peak/Remove -Inf **");
		
		for (int i=0; i<num; ++i) {
			String k = map.peakKey();
			Double v = map.get(k);
			assertTrue(v == Double.NEGATIVE_INFINITY);
//			assertEquals(k, String.format("I%d", i));
			map.remove(k);
			System.out.println(map);
			assertTrue(map.checkState());
		}
		
		System.out.println("** Test Peak/Remove **");

		for (int i=0; i<num/2; ++i) {
			String k = map.peakKey();
			Double v = map.get(k);
			assertTrue(v == i);
			assertEquals(k, String.format("K%d", i));
			map.remove(k);
			System.out.println(map);
			assertTrue(map.checkState());
		}
		
		System.out.println("** Test Update **");
		for (int i=num/2; i<num; ++i) {
			map.updateValue(String.format("K%d", i), new Double(-i));
			System.out.println(map);
			assertTrue(map.checkState());
		}

		System.out.println("** Test Peak/Remove after Update **");		
		for (int i=num-1; i>=num/2; --i) {
			String k = map.peakKey();
			Double v = map.get(k);
			assertTrue(v == -i);
			assertEquals(k, String.format("K%d", i));
			map.remove(k);
			System.out.println(map);
			assertTrue(map.checkState());			
		}
	}
}

package net.linpengt.collections;

import static org.junit.Assert.*;

import org.junit.Test;

public class HeapMapTest {
	
	@Test
	public void testHeapMap() {
		int num = 20;
		System.out.println("** Test Put **");
		HeapMap<String, Integer> map = new HeapMap<String, Integer>();
		for (int i=num-1; i>=0; --i) {
			map.put(String.format("K%d", i), i);
			System.out.println(map);
			assertTrue(map.checkState());
		}
		
		System.out.println("** Test Peak/Remove **");
		for (int i=0; i<num/2; ++i) {
			String k = map.peakKey();
			Integer v = map.get(k);
			assertTrue(v == i);
			assertEquals(k, String.format("K%d", i));
			map.remove(k);
			System.out.println(map);
			assertTrue(map.checkState());
		}
		
		System.out.println("** Test Update **");
		for (int i=num/2; i<num; ++i) {
			map.updateValue(String.format("K%d", i), -i);
			System.out.println(map);
			assertTrue(map.checkState());
		}

		System.out.println("** Test Peak/Remove after Update **");		
		for (int i=num-1; i>=num/2; --i) {
			String k = map.peakKey();
			Integer v = map.get(k);
			assertTrue(v == -i);
			assertEquals(k, String.format("K%d", i));
			map.remove(k);
			System.out.println(map);
			assertTrue(map.checkState());			
		}
	}
}

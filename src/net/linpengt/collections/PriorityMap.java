package net.linpengt.collections;

import java.util.Map;

public interface PriorityMap<K, V extends Comparable<V>> extends Map<K, V> {
	/**
	 * retrieves, but does not remove the first key in the Map
	 */
	public K peakKey();
	
	/**
	 * retrieve and remove the first entry
	 */
	public Entry<K, V> poll();
	
	/**
	 * update the value corresponding to an existing key
	 * may throw an exception if key is not found
	 */
	public void updateValue(K key, V new_value);
	
}

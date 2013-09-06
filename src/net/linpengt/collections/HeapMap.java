package net.linpengt.collections;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

public class HeapMap<K, V extends Comparable<V>> implements PriorityMap<K, V>{

	HashMap<K, V> entries;
	BidiMap<K, Integer> key2pos;
	ArrayList<V> heap;
	
	public HeapMap() {
		entries = new HashMap<K, V>();
		key2pos = new DualHashBidiMap<K, Integer>();
		heap = new ArrayList<V>();
	}
	
	@Override
	public int size() {
		return entries.size();
	}

	@Override
	public boolean isEmpty() {
		return entries.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return entries.containsKey(key);
	}

	// this operation is O(N)
	@Override
	public boolean containsValue(Object value) {
		return entries.containsValue(value);
	}

	@Override
	public V get(Object key) {
		return entries.get(key);
	}

	@Override
	public V put(K key, V value) {
		V old_value = entries.put(key, value);
		if (old_value != null) {
			Integer pos = key2pos.get(key);
			heap.set(pos, value);
			bubbleUpDown(pos);
		}
		else{
			heap.add(value);
			Integer pos = heap.size() - 1;
			key2pos.put(key, pos);
			bubbleUp(pos);
		}
		return old_value;
	}

	@Override
	public V remove(Object key) {
		V value = entries.remove(key);
		if (value != null) {
			Integer pos = key2pos.get(key);
			heapRemove(pos);
			key2pos.remove(key);
		}
		return null;
	}

	/**
	 * This implementation is not yet efficient
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		Iterator<?> iter = m.entrySet().iterator();
		while(iter.hasNext()) {
			Map.Entry pair = (Map.Entry) iter.next();
			put((K) pair.getKey(), (V) pair.getValue());
		}
	}

	@Override
	public void clear() {
		entries.clear();
		key2pos.clear();
		heap.clear();
	}

	@Override
	public Set<K> keySet() {
		return entries.keySet();
	}

	@Override
	public Collection<V> values() {
		return entries.values();
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return entries.entrySet();
	}

	@Override
	public K peakKey() {
		if (isEmpty())
			return null;
		else
			return key2pos.getKey(0);
	}

	@Override
	public void updateValue(K key, V new_value) {
		put(key, new_value);
	}
	
	protected void swap(int pos1, int pos2) {
		assert pos1 < heap.size() && pos2 < heap.size();
		V v1 = heap.get(pos1);
		V v2 = heap.get(pos2);
		K k1 = key2pos.getKey(pos1);
		K k2 = key2pos.getKey(pos2);
		heap.set(pos1, v2);
		heap.set(pos2, v1);
		key2pos.put(k1, pos2);
		key2pos.put(k2, pos1);
	}
	
	/**
	 * Bubble up the element at {@code pos}
	 * @param pos
	 */
	protected void bubbleUp(int pos) {
		assert pos < heap.size();
		while (pos > 0) {
			int p0 = (pos-1)/2;
			V v1 = heap.get(pos);
			V v0 = heap.get(p0);
			if (v0.compareTo(v1) <= 0)
				break;
			swap(p0, pos);
			pos = p0;
		}
	}
	
	protected void bubbleDown(int pos) {
		assert pos < heap.size();
		while(pos*2+1 < heap.size()) {
			int p2 = pos*2+1;
			if (pos*2+2 < heap.size() && heap.get(pos*2+2).compareTo(heap.get(p2)) < 0)
				p2 = pos*2+2;
			if (heap.get(p2).compareTo(heap.get(pos)) < 0)
				swap(pos, p2);
			pos = p2;
		}
	}

	/**
	 * Remove one element by first swapping it with the last element of the heap,
	 * remove it, then bubble up/down the original last element
	 * @param pos
	 */
	protected void heapRemove(int pos) {
		assert pos < heap.size();
		if (pos == heap.size() - 1) {
			heap.remove(pos);
			return;
		}
			
		int last = heap.size() - 1;
		swap(pos, last);
		heap.remove(last);
		bubbleUpDown(pos);
	}
	
	/**
	 * Bubble up and/or bubble down an element in the heap
	 * @param pos
	 */
	protected void bubbleUpDown(int pos) {
		bubbleUp(pos);
		bubbleDown(pos);
	}
	
	/**
	 * check the consistency of the state
	 * @return whether the heap map is consistent
	 */
	public boolean checkState() {
		if (entries.size() != key2pos.size() || key2pos.size() != heap.size())
			return false;
		for (int i=0; i< heap.size(); ++i) {
			V value = heap.get(i);
			K key = key2pos.getKey(i);
			if (entries.get(key) != value) {
				return false;
			}
		}
		for (int i=1; i < heap.size(); ++i) {
			if (heap.get((i-1)/2).compareTo(heap.get(i)) > 0)
				return false;
		}
		return true;		
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("HeapMap: size=%d\n", size()));
		sb.append(String.format("\tentries: %s\n", trimMap(entries)));
		sb.append(String.format("\tkey2pos: %s\n", trimBidiMap(key2pos)));
		sb.append(String.format("\theap:    %s\n", trimList(heap)));
		return sb.toString();
	}
	
	static final int MAX_ENTRIES = 20;
	
	private static<V> List<V> trimList(List<V> list) {
		return list.subList(0, Math.min(MAX_ENTRIES, list.size()));
	}
	private static<K, V> Map<K, V> trimMap(Map<K, V> map) {
		Map<K, V> res = new HashMap<K, V>();
		int i = 0;
		for (Map.Entry<K, V> ent : map.entrySet()) {
			res.put(ent.getKey(), ent.getValue());
			if (++i >= MAX_ENTRIES)
				break;
		}
		return res;
	}

	private static<K, V> Map<K, V> trimBidiMap(BidiMap<K, V> map) {
		Map<K, V> res = new HashMap<K, V>();
		int i = 0;
		for (Map.Entry<K, V> ent : map.entrySet()) {
			res.put(ent.getKey(), ent.getValue());
			if (++i >= MAX_ENTRIES)
				break;
		}
		return res;
	}

	@Override
	public Map.Entry<K, V> poll() {
		if (isEmpty())
			return null;
		K key = key2pos.getKey(0);
		V value = heap.get(0);
		remove(key);
		
		return new AbstractMap.SimpleEntry<K, V>(key, value);
	}
}

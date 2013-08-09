# java-kit

Useful Java Tools written/collected by Linpeng Tang

## Collections

### PriorityMap & HeapMap
A Priority Map is a map that allows you to retrieve/remove the entry with the smallest key with high efficiency. In this library, Priority Map is impelemented by `HeapMap`.

You can use it to implement a Priority Queue that allows you to update the priority value of the keys (just update the value of that key).

For example,

	PriorityMap<String, Integer> map = new HeapMap<String, Integer>();
	map.put("A", 1);
	map.put("B", 2);
	
creates a Priority Map with 2 entries. `map.peakKey()` will now return `"A"`. Now go ahead and change the values:

	map.put("A", 3);
	
Now `map.peakKey()` will return `"B"`.

Many caching policies can be implemented with Priority Map.	
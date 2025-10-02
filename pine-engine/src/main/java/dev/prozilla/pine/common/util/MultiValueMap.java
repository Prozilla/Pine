package dev.prozilla.pine.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Implementation of a hash map which supports multiple values per key.
 * @param <K> The type of keys in this map
 * @param <V> The type of values in this map
 */
public class MultiValueMap<K, V> extends HashMap<K, List<V>> {
	
	public V getFirst(K key) {
		List<V> values = get(key);
		if (values == null) {
			return null;
		}
		return values.getFirst();
	}
	
	public V getLast(K key) {
		List<V> values = get(key);
		if (values == null) {
			return null;
		}
		return values.getLast();
	}
	
	public V get(K key, int index) {
		List<V> values = get(key);
		if (values == null) {
			return null;
		}
		return values.get(index);
	}
	
	public List<V> add(K key, V value) {
		if (!containsKey(key)) {
			put(key, new ArrayList<>());
		}
		List<V> values = get(key);
		values.add(value);
		return values;
	}
	
	public List<V> addAll(K key, Collection<? extends V> newValues) {
		if (!containsKey(key)) {
			put(key, new ArrayList<>());
		}
		List<V> values = get(key);
		values.addAll(newValues);
		return values;
	}
	
	public V removeValue(K key, V value) {
		List<V> values = get(key);
		if (values == null) {
			return null;
		}
		values.remove(value);
		return value;
	}
	
	public List<V> set(K key) {
		return super.put(key, new ArrayList<>());
	}
	
	public List<V> set(K key, V value) {
		return super.put(key, ListUtils.createSingleton(value));
	}
	
	public List<V> set(K key, Collection<? extends V> values) {
		return super.put(key, new ArrayList<>(values));
	}
	
	public List<V> setIfAbsent(K key, V value) {
		return super.putIfAbsent(key, ListUtils.createSingleton(value));
	}
	
	public List<V> flatValues() {
		List<V> flatValues = new ArrayList<>();
		for (List<V> values : values()) {
			flatValues.addAll(values);
		}
		return flatValues;
	}
	
	public int valueSize(K key) throws IllegalArgumentException {
		if (!containsKey(key)) {
			throw new IllegalArgumentException("Key has no corresponding value: " + key);
		}
		return get(key).size();
	}
	
}

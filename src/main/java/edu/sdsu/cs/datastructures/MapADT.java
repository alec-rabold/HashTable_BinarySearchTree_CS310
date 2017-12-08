package edu.sdsu.cs.datastructures;

import java.util.Iterator;
public interface MapADT<K extends Comparable<K>, V> {
    /**
     * Returns true if the map has an object for the corresponding key.
     * @param key object to search for
     * @return true if within map, false otherwise
     */ boolean contains(K key);
    /**
     * Adds the given key/value pair to the map.
     * @param key Key to add to the map
     * @param value Corresponding value to associate with the key
     * @return the previous value associated with this key or null if new
     */
    V add(K key, V value);
    /**
     * Removes the key/value pair identified by the key parameter from the map.
     * @param key item to remove
     * @return true if removed, false if not found or unable to remove
     */
    boolean delete(K key);
    /**
     * Returns the value associated with the parameter key.
     * @param key key to lookup in the map
     * @return Value associated with key or null if not found
     */
    V getValue(K key);
    /**
     * Returns the first key found with the parameter value.
     * @param value value to locate
     * @return key of first item found with the matching value
     */
    K getKey(V value);
    /**
     * Identifies the size of the map.
     * @return Number of entries stored in the map.
     */
    int size();
    /**
     * Indicates if the map contains nothing.
     * @return true if the map is empty, as the method cryptically indicates.
     */
    boolean isEmpty();
    /**
     * Resets the map to an empty state with no entries.
     */
    void clear();
    /**
     * Provides a key iterator.
     * @return Iterator over the keys (some data structures provided sorted)
     */
    Iterator<K> keys();
    /**
     * Provides a value iterator. The values arrive corresponding to their
     * keys in the key order.
     * @return Iterator over the values.
     */
    Iterator<V> values();
}

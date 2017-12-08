package edu.sdsu.cs.datastructures;

import java.util.*;
import java.math.*;

/**
 * @author ALEC RABOLD, cssc0185
 */

public class HashTable<K extends Comparable<K>, V> implements MapADT<K, V> {
    private int size;
    private List<Entry<K, V>>[] buckets;
    /**
     * Private data structure with key and value
     */
    private class Entry<K, V> {
        private K key;
        private V value;
        public Entry<K, V> next;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public HashTable() {
        this(17); // Prime number
    }
    public HashTable(int capacity) {
        size = 0;
        buckets = new List[Math.max(capacity, 17)];
        for(int curBin = 0; curBin < buckets.length; curBin++) {
            buckets[curBin] = new LinkedList<>();
        }
    }

    /**
     * Returns the first key found with the parameter value.
     * @param value value to locate
     * @return key of first item found with the matching value
     */

    public K getKey(V value) {
        for(int i = 0; i < buckets.length; i++) {
            List<Entry<K, V>> curBin = buckets[i];
            for(int j = 0; j < curBin.size(); j++) {
                if(value.equals(curBin.get(j).value)) {
                    return curBin.get(j).key;
                }
            }
        }
        return null;
    }

    /**
     * Returns the value associated with the parameter key.
     * @param key key to lookup in the map
     * @return Value associated with key or null if not found
     */

    public V getValue(K key) {
        checkKey(key);
        List<Entry<K, V>> bucket = buckets[findIndex(key)];
        if(bucket.isEmpty()) return null;
        for(int i = 0; i < bucket.size(); i++) {
            if(key.compareTo(bucket.get(i).key) == 0) {
                return bucket.get(i).value;
            }
        }
        return null;
    }

    /**
     * Adds the given key/value pair to the map.
     * @param key Key to add to the map
     * @param value Corresponding value to associate with the key
     * @return the previous value associated with this key or null if new
     */

    public V add(K key, V value) {
        checkKey(key);
        int index = findIndex(key);
        List<Entry<K, V>> bucket = buckets[index];
        for(int i = 0; i < bucket.size(); i++) {
            if (key.compareTo(bucket.get(i).key) == 0) {
                V oldVal = bucket.get(i).value;
                bucket.get(i).value = value;
                return oldVal;
            }
        }

        Entry<K, V> newEntry = new Entry<>(key, value);
        buckets[index].add(newEntry);
        size++;

        // Check if at/above 75% threshold
        if(isFull()) {
            resizeUp();
        }

        return null;
    }

    /**
     * Removes the key/value pair identified by the key parameter from the map.
     * @param key item to remove
     * @return true if removed, false if not found or unable to remove
     */

    public boolean delete(K key) {
        checkKey(key);
        List<Entry<K, V>> bucket = buckets[findIndex(key)];
        if(bucket.isEmpty()) return false;
        for(int i = 0; i < bucket.size(); i++) {
            if(key.compareTo(bucket.get(i).key) == 0) {
                bucket.remove(i);
                size--;
                if(isSpacious()) {
                    resizeDown();
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the map has an object for the corresponding key.
     * @param key object to search for
     * @return true if within map, false otherwise
     */
    public boolean contains(K key) {
        return (getValue(key) != null);
    }

    /**
     * Identifies the size of the map.
     * @return Number of entries stored in the map.
     */
    public int size() {
        return size;
    }

    /**
     * Indicates if the map contains nothing.
     * @return true if the map is empty, as the method cryptically indicates.
     */

    public boolean isEmpty() {
        return (size == 0);
    }

    /**
     * Resets the map to an empty state with no entries.
     */
    public void clear() {
        buckets = new List[17];
        size = 0;
        for(int curBin = 0; curBin < buckets.length; curBin++) {
            buckets[curBin] = new LinkedList<>();
        }
    }

    /**
     * Provides a key iterator.
     * @return Iterator over the keys (some data structures provided sorted)
     */
    public Iterator<K> keys() {
        Iterator<K> it = new Iterator<K>() {
            private int binIndex = 0; // Range: 0 - size
            private int binListInd = 0;

            @Override
            public boolean hasNext() {
                for (int i = binIndex; i < buckets.length; i++) {
                    for (int j = binListInd; j < buckets[i].size(); j++) {
                        if (buckets[i].get(j) != null) return true;
                    }
                }
                return false;

            }

            @Override
            public K next() {
                for (; binIndex < buckets.length; binIndex++) {
                    for (; binListInd < buckets[binIndex].size(); binListInd++) {
                        if (buckets[binIndex].get(binListInd) != null) {
                            K res = buckets[binIndex].get(binListInd).key;

                            // Increment up one
                            if(binListInd == (buckets[binIndex].size() - 1)) {
                                binIndex++;
                                binListInd = 0;
                            }
                            else binListInd++;

                            return res;
                        }
                    }
                }
                return null;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return it;
    }

    /**
     * Provides a value iterator. The values arrive corresponding to their
     * keys in the key order.
     * @return Iterator over the values.
     */
    public Iterator<V> values() {
        Iterator<V> it = new Iterator<V>() {
            private int binIndex = 0; // Range: 0 - size
            private int binListInd = 0;

            @Override
            public boolean hasNext() {
                for (int i = binIndex; i < buckets.length; i++) {
                    for (int j = binListInd; j < buckets[i].size(); j++) {
                        if (buckets[i].get(j) != null) return true;
                    }
                }
                return false;

            }

            public V next() {
                for (; binIndex < buckets.length; binIndex++) {
                    for (; binListInd < buckets[binIndex].size(); binListInd++) {
                        if (buckets[binIndex].get(binListInd) != null) {
                            V res = buckets[binIndex].get(binListInd).value;

                            // Increment up one
                            if(binListInd == (buckets[binIndex].size() - 1)) {
                                binIndex++;
                                binListInd = 0;
                            }
                            else binListInd++;

                            return res;
                        }
                    }
                }
                return null;
            }


            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return it;
    }

    private int findIndex(K key) {
        return key.hashCode() % buckets.length;
    }
    private void checkKey(K key) {
        assert key != null : "Null keys are not allowed";
    }
    private boolean isFull() {
        return size >= 0.75 * buckets.length;
    }
    private boolean isSpacious() {
        return size <= 0.15 * buckets.length;
    }
    private long nextPrime(long n)
    {
        BigInteger b = new BigInteger(String.valueOf(n));
        return Long.parseLong(b.nextProbablePrime().toString());
    }
    private void resizeUp() {
        List<Entry<K, V>>[] oldBuckets = buckets;
        int newCapacity = (int)nextPrime((long)(oldBuckets.length * 1.75)); // next prime after 1.75 x original capacity
        buckets = new List[newCapacity];
        for(int curBin = 0; curBin < buckets.length; curBin++) {
            buckets[curBin] = new LinkedList<>();
        }
        size = 0;

        for(int i = 0; i < oldBuckets.length; i++) {
            List<Entry<K, V>> curBucket = oldBuckets[i];
            for(int j = 0; j < curBucket.size(); j++) {
                add(curBucket.get(j).key, curBucket.get(j).value);
            }
        }
    }

    private void resizeDown() {
        List<Entry<K, V>>[] oldBuckets = buckets;
        int newCapacity = (int)nextPrime((long)(oldBuckets.length * 0.25)); // find next prime after 25% of original size
        buckets = new List[newCapacity];
        for(int curBin = 0; curBin < newCapacity; curBin++) {
            buckets[curBin] = new LinkedList<>();
        }
        int tempSize = size();
        size = 0;

        for(int i = 0; i < oldBuckets.length; i++) {
            List<Entry<K, V>> curBucket = oldBuckets[i];
            for(int j = 0; j < curBucket.size(); j++) {
                add(curBucket.get(j).key, curBucket.get(j).value);
            }
        }

       //  size = tempSize;
    }


}




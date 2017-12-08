package edu.sdsu.cs.datastructures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author ALEC RABOLD, cssc0185
 */

public class BinarySearchTree<K extends Comparable<K>, V> implements MapADT<K, V>{

    private Node<K, V> root;
    private int size = 0;

    private static class Node<K, V> {
        private K data;
        private V value;
        private Node<K, V> left;
        private Node<K, V> right;

        private Node(K data, V value) {
            setData(data);
            setValue(value);
        }

        private K getData() {
            return data;
        }

        private void setData(K data) {
            this.data = data;
        }

        private V getValue() {
            return value;
        }

        private void setValue(V value) {
            this.value = value;
        }
    }

    /**
     * Adds the given key/value pair to the map.
     * @param data Key to add to the map
     * @param value Corresponding value to associate with the key
     * @return the previous value associated with this key or null if new
     */
    public V add(K data, V value) {
        V res = null;
        if(contains(data)) res = getValue(data);
        add(data, value, root);
        return res;
    }



    /**
     * Recursive add method (helper)
     */
    private Node<K, V> add(K data, V value, Node<K, V> node) {
        if(node != null) {
            if(data == null)
                node.right = add(data, value, node.right);
            else {
                if(node.getData() == null) {
                    node = new Node<>(data, value);
                    node.right = add(null, null, node.right);
                }
                if(data.compareTo(node.data) < 0) {
                    node.left = add(data, value, node.left);
                }
                if(data.compareTo(node.data) > 0) {
                    node.right = add(data, value, node.right);
                }
                if(data.compareTo(node.data) == 0) {
                    node.setValue(value);
                }
            }
        }
        else {
            node = new Node<>(data, value);
            size++;
        }

        if(root == null)
            root = node;
        return node;
    }

    /**
     * Removes the key/value pair identified by the key parameter from the map.
     * @param data item to remove
     * @return true if removed, false if not found or unable to remove
     */
    public boolean delete(K data) {
        if(contains(data)) {
            root = remove(root, data);
            return true;
        }
        return false;
    }
    private Node<K, V> remove(Node<K, V> here, K data) {
        if(here != null) {
            if(data == null) {
                here.right = remove(here.right, data);
                if(here.getData() == null) {
                    here = removeNode(here);
                }
            }
            else {
                if(data.compareTo(here.getData()) < 0) {
                    here.left = remove(here.left, data);
                }
                else if(data.compareTo(here.getData()) > 0) {
                    here.right = remove(here.right, data);
                }
                else {
                    here = removeNode(here);
                }
            }
        }

        return here;
    }
    private Node<K, V> removeNode(Node<K, V> here) {
        if(here.left == null) {
            here = here.right;
        }
        else if(here.right == null) {
            here = here.left;
        }
        else {
            Node<K, V> big = here.left;
            Node<K, V> last = null;
            while(big.right != null) {
                last = big;
                big = big.right;
            }
            here.data = big.data;
            if(last == null) {
                here.left = big.left;
            }
            else {
                last.right = big.left;
            }
        }

        return here;
    }

    /**
     * Returns the value associated with the parameter key.
     * @param data key to lookup in the map
     * @return Value associated with key or null if not found
     */
    public V getValue(K data) {
        Node<K, V> loc = search(data, root);
        return (loc == null) ? null : loc.getValue();
    }

    /**
     * Returns the first key found with the parameter value.
     * @param value value to locate
     * @return key of first item found with the matching value
     */
    public K getKey(V value) {
        List<Node> list = inOrder();
        for(Node n : list) {
            if(n.getValue().equals(value))
                return (K)n.getData();
        }
        return null;
    }

    /**
     * Provides a key iterator.
     * @return Iterator over the keys (some data structures provided sorted)
     */
    public Iterator<K> keys() {
        Iterator<K> it = new Iterator<K>() {
            private List<Node> list = inOrder(); // ArrayList
            private int curIndex;

            @Override
            public boolean hasNext() {
                return (curIndex < list.size());
            }

            @Override
            public K next() {
                return (K) list.get(curIndex++).getData();
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
            private List<Node> list = inOrder(); // ArrayList
            private int curIndex;

            @Override
            public boolean hasNext() {
                return (curIndex < (list.size() - 1));
            }

            @Override
            public V next() {
                return (V) list.get(curIndex++).getValue();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return it;
    }

    private List<Node> inOrder() {
        List<Node> list = new ArrayList<>();
        inOrder(root, list);
        return list;
    }

    private void inOrder(Node<K, V> node, List<Node> list) {
        if(node != null) {
            inOrder(node.left, list);
            list.add(node);
            inOrder(node.right, list);
        }
    }



    /**
     * Returns true if the map has an object for the corresponding key.
     * @param data object to search for
     * @return true if within map, false otherwise
     */
    public boolean contains(K data) {
        if(root == null)
            return false;
        else
            return (search(data, root) != null);
    }
    private Node<K, V> search(K data, Node<K, V> node) {
        if (data != null) {
            if(data.compareTo(node.getData()) == 0)
                return node;
            else if(data.compareTo(node.getData()) < 0) {
                if(node.left == null)
                    return null;
                else
                    return search(data, node.left);
            }
            else if(data.compareTo(node.getData()) > 0) {
                if(node.right == null)
                    return null;
                else
                    return search(data, node.right);
            }
        }
        else {
            Node<K, V> max = root;
            while(max != null) {
                if(max.getData() == null)
                    return node;
                max = max.right;
            }
        }
        return null;
    }

    /**
     * Indicates if the map contains nothing.
     * @return true if the map is empty, as the method cryptically indicates.
     */
    public boolean isEmpty() {
        return (root == null);
    }

    /**
     * Resets the map to an empty state with no entries.
     */
    public void clear() {
        root = null;
    }

    /**
     * Identifies the size of the map.
     * @return Number of entries stored in the map.
     */
    public int size() {
        return this.size;
    }

}


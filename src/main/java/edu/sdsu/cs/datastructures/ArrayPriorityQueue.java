package edu.sdsu.cs.datastructures;

import java.util.*;

/**
 * An array-based priority managedList implementation.
 * <p>
 * A first-in-first-out (FIFO) data container, the basic managedList represents
 * a line arranged in the order the items arrive. This managedList differs from
 * the standard implementation in that it requires all enclosed objects
 * support comparison with like-type objects (Comparable), and it
 * automatically arranges the items in the managedList based upon their ranking.
 * Lower items appear closer to the managedList's front than larger items.
 * </p>
 * Due to their nature, queues must support rapid enqueue (offer) and
 * dequeue (poll) operations. That is, getting in and out of line must
 * occur rapidly. In this priority managedList, the poll operation shall occur
 * in constant time, but because the managedList requires arrangement, adding
 * to the circular-array based priority managedList may produce slower timings,
 * for the data structure must use binary-search to locate where the new
 * item belongs in relation to everything else, and it must then shift
 * the existing contents over to make room for the addition.
 * </p>
 * <p>
 * In the likely event two objects share the same priority, new items
 * added to the managedList shall appear after existing entries. When a standard
 * priority customer enters the line, it should naturally go behind the
 * last standard customer currently in line.
 * </p>
 * <p>
 * Although technically a Collection object, the managedList does not support
 * most of the standard Collection operations. That is, one may not add
 * or remove from the Queue using the collection methods, for it breaks
 * the abstraction. One may, however, use a Queue object as an input
 * parameter to any other Collection object constructor.
 * </p>
 *
 * @param <E> Object to store in container. It must support comparisons with
 *            other objects of the same type.
 * @author Alec Rabold, cssc0185@edoras.sdsu.edu
 */
public final class ArrayPriorityQueue<E extends Comparable<E>>
        extends AbstractCollection<E> implements Queue<E> {

    private final List<E> managedList = new CirArrayList<>();
    /**
     * Builds a new, empty priority managedList.
     */
    public ArrayPriorityQueue() {

    }


    /**
     * Builds a new priority managedList containing every item in the provided
     * collection.
     *
     * @param col the Collection containing the objects to add to this
     *            managedList.
     */
    public ArrayPriorityQueue(Collection<? extends E> col) {
        // this()
        for (E itemInExistingCollection : col) {
            offer(itemInExistingCollection);
        }
    }

    /**
     * Inserts the specified element into this managedList if it is possible to do
     * so immediately without violating capacity restrictions. When using a
     * capacity-restricted managedList, this method is generally preferable to add
     * (E), which can fail to insert an element only by throwing an exception.
     *
     * @param e the element to add
     * @return true if element added to managedList, false otherwise
     */
    @Override
    public boolean offer(E e) {
        try {
            // 2+ elements needed for binary search
            if(managedList.size() == 0)
                managedList.add(e);
            else if (managedList.size() == 1) {
                if(e.compareTo(managedList.get(0)) < 0)
                    managedList.add(0,e);
                else
                    managedList.add(e);
            }
            else {
                int posToAdd = bSearchPos(e);
                if(posToAdd == -1) // -1 marks add-to-front
                    managedList.add(e);
                else
                    managedList.add(posToAdd, e);
            }
            return true;
        }
        catch(Exception exc) {
            // Element not added
            return false;
        }
    }

    /**
     * Binary Search algorithm implementation
     *
     * returns when indices are equal (spot to insert)
     * if lo == size() -1, test whether this new element is greater than
     * the current tail. If so, return a flag to indicate use of add(e)
     * instead of add(int, e) [add to back]
     *
     * @return position to insert element (returns -1 to indicate add to end)
     */
    private int bSearchPos(E val) throws Exception {
        int lo = 0;
        int hi = managedList.size() - 1;
        int mid = 0;
        while (lo <= hi) {
            if(lo == hi) {
                if ((size() > 2) && (lo == (size() - 1))) {
                    if (val.compareTo(managedList.get(size() - 1)) > 0) return -1;
                    else return lo;
                }
                else return lo;
            }
            mid = lo + ((hi - lo) / 2);
            if (val.compareTo(managedList.get(mid)) < 0) hi = mid ;
            else if (val.compareTo(managedList.get(mid)) > 0) lo = mid + 1;
            else lo = mid+1;
        }
        throw new Exception("The binary search function failed");

    }

    /**
     * Retrieves and removes the head of this managedList. This method
     * differs from
     * poll only in that it throws an exception if this managedList is empty.
     *
     * @return the managedList's head
     * @throws java.util.NoSuchElementException if this managedList is empty
     */
    @Override
    public E remove() {
        if(!managedList.isEmpty())
            return managedList.remove(0);
        else
            throw new NoSuchElementException();
    }

    /**
     * Retrieves and removes the head of this managedList, or returns null if
     * this
     * managedList is empty.
     *
     * @return this managedList's head or null if this managedList is empty.
     */
    @Override
    public E poll() {
        if(!managedList.isEmpty())
            return managedList.remove(0);
        else
            return null;
    }

    /**
     * Retrieves, but does not remove, the head of this managedList. This method
     * differs from peek only in that it throws an exception if this managedList is
     * empty.
     *
     * @return the head of this managedList
     * @throws java.util.NoSuchElementException if this managedList is empty
     */
    @Override
    public E element() {
        if(!managedList.isEmpty())
            return managedList.get(0);
        else
            throw new NoSuchElementException();
    }

    /**
     * Retrieves, but does not remove, the head of this managedList, or returns
     * null if this managedList is empty.
     *
     * @return the head of this managedList or null if this managedList is empty
     */
    @Override
    public E peek() {
        if(!managedList.isEmpty())
            return managedList.get(0);
        else
            return null;
    }

    /**
     * Returns an iterator over the elements in this collection. The iterator
     * produces results in the order in which they would appear were one to
     * successively poll the managedList.
     *
     * @return an Iterator over the elements in this managedList.
     */
    @Override
    public Iterator<E> iterator() {
        return managedList.iterator();
    }

    /**
     * Reports the number of items in this managedList.
     *
     * @return the number of items in this managedList.
     * @implNote it is INCORRECT to track this as a field in this class.
     */
    @Override
    public int size() {
        return managedList.size();
    }
}

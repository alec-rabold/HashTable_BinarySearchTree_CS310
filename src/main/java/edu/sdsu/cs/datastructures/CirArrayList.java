package edu.sdsu.cs.datastructures;

import java.util.*;

/**
 * A circular version of an array list.
 * <p>Operates as a standard java.util.ArrayList, but additions and removals
 * from the List's front occur in constant time, for its circular nature
 * eliminates the shifting required when adding or removing elements to the
 * front of the ArrayList.
 * </p>
 *
 * @author Alec Rabold, cssc0185
 */
public final class CirArrayList<E> extends AbstractList<E> implements
        List<E>, RandomAccess {

    private E[] data;

    private int curSize;

    private int head;
    private int tail;

    /**
     * Builds a new, empty CirArrayList.
     */
    public CirArrayList() {
        // todo: default constructor
        head = tail = curSize = 0;
        data = (E[])new Object[10];
    }

    /**
     * Constructs a new CirArrayList containing all the items in the input
     * parameter.
     *
     * @param col the Collection from which to base
     */
    public CirArrayList(Collection<? extends E> col) {
        // todo: collection constructor
        this();
        for( E thing : col)
            add(size(), thing);
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index (0 based) of the element to return.
     * @return element at the specified position in the list.
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0
     *                                   || index >= size())
     */
    @Override
    public E get(int index) {
        // todo: Students must code
        checkRange(index); // throws IndexOOB Exception if out of range
        return data[calculate(index)];
    }


    /**
     * Checks whether the index is out of range
     *
     * @param index of element to check range of.
     * @return void
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0
     *                                   || index >= size())
     *
     */
    private void checkRange(int index) {
        if ((index < 0 || index >= curSize))
            throw new IndexOutOfBoundsException("Index: " + index + " is out of bounds. Size = " + size());
    }

    /**
     * Calculates real position in array given a logical position
     *
     * @param index (logical)
     * @return real index in circular array
     *
     */
    private int calculate(int index) {
        return (head + index) % data.length;
    }

    /**
     * Checks the capacity and resizes if above 75% capacity threshold
     *
     * @return void
     *
     */
    private void ensureCapacity() {
        if(size() >= (0.75 * data.length)) // size() == curSize
            resize(2 * size());
    }

    /**
     * Resizes array (2x original size)
     *
     * @return void
     *
     */
    private void resize(int newCapacity) {

        E[] temp = (E[]) new Object[newCapacity];
        for(int i = 0; i < size(); i++)
            temp[i] = data[calculate(i)];
        data = temp;
        head = 0;
        tail = size()-1;


    }

    /**
     * Replaces the element at the specified position in this list with the
     * specified element.
     *
     * @param index index of the element to replace
     * @param value element to be stored at the specified position
     * @return element previously at the specified position
     * @throws IndexOutOfBoundsException if index is out of the range (index < 0
     *                                   || index >= size())
     */
    @Override
    public E set(int index, E value) {
        // todo: Students must code
        checkRange(index);
        int pos = calculate(index);
        E oldVal = data[pos];
        data[pos] = value;
        return oldVal;
    }

    /**
     * Inserts the specified element at the specified position in this list.
     * Shifts the element currently at that position (if any) and any
     * subsequent elements to the right (adds one to their indices).
     *
     * @param index index at which the specified element is to be inserted
     * @param value element to be inserted
     */
    @Override
    public void add(int index, E value) {
        // todo: Students must code
        ensureCapacity();
        int pos = calculate(index); // True position in array

        // if there are no elements, set head to the position (index 0, position ??)
        // *protects against divide by zero in modulus*
        if(size() == 0) {
            data[pos] = value;
            head = pos;
            tail = pos;
            // tail = (pos+1)%data.length;
        }
        // if the logical position is the head, then insert right before head and reassign head-variable
        // *tail stays the same*
        else if(index == 0) {
            int i = (head-1);
            if (i < 0) i = data.length-1;
            head = i;
            data[head] = value;
        }
        else{
            // shift all array contents after pos right 1
            for(int i = (pos + size() - index); i > pos; i--)
                data[i % data.length] = data[(i-1) % data.length];
            data[pos] = value;
            tail = (tail + 1) % data.length;
        }
        curSize++;
    }

    /**
     * Removes the element at the specified position in this list.  Shifts
     * any subsequent elements to the left (subtracts one from their indices).
     * Returns the element that was removed from the list.
     *
     * @param index index of element to remove
     * @return the element previously at the specified position.
     */
    @Override
    public E remove(int index) {
        // todo: Students must code
        int pos = calculate(index);

        E temp = data[pos];
        data[pos] = null;
        if(pos == head)
            head = (head+1) % data.length;
        else if(pos == tail)
            tail = (tail-1) % data.length;
        else {
            // shift all array contents after pos left 1
            for(int i = pos; i < (pos + curSize-index-1); i++)
                data[calculate(i)] = data[calculate(i+1)];
            tail = (tail-1) % data.length;
        }
        curSize--;
        return temp;
    }

    /**
     * Reports the number of items in the List.
     *
     * @return the item count.
     */
    @Override
    public int size() {
        return curSize;
    }
}

package edu.sdsu.cs.datastructures;

import edu.sdsu.cs.util.IValueGenerator;
import edu.sdsu.cs.util.NameGenerator;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Example unit tests: We will flesh these out in class ...
 */
public class ArrayPriorityQueueTest extends TestCase {

    private static final int DEFAULT_TEST_SIZE = 8086;
    private static final int DEFAULT_NUM_EPOCH = 4;
    private static final List<String> TEST_NAMES = generateTestValues
            (DEFAULT_TEST_SIZE << DEFAULT_NUM_EPOCH, new NameGenerator());

    private Queue<String> sut;

    private static List<String> generateTestValues(int count,
                                                   IValueGenerator<String>
                                                           generator) {
        List<String> testVals = new java.util.ArrayList<>();
        for (int num = 0; num < count; num++) {
            testVals.add(generator.generate());
        }
        return testVals;
    }

    public void setUp() throws Exception {
        super.setUp();
        sut = new ArrayPriorityQueue<>();
    }

    public void test_constructorDefault_correctInitialValues() {
        assertThat(sut.size(), is(0));
        assertNull(sut.peek());
    }

    public void test_constructorCollection_correctInitialValues() {
        final List<String> testNames = TEST_NAMES.subList(0, DEFAULT_TEST_SIZE);
        sut = new ArrayPriorityQueue<>(testNames);

        assertThat(sut.size(), is(DEFAULT_TEST_SIZE));
        assertTrue(sut.containsAll(testNames));
    }

    public void test_pollFromEmpty_returnsNull() {
        assertNull(sut.poll());
    }

    public void test_elementFromInitiallyEmpty_exception() {
        try {
            sut.element();
            fail("NoSuchElementException expected.");
        } catch (NoSuchElementException e) {
            assertTrue(true);
        }
    }

    public void test_removeFromInitiallyEmpty_exception() {
        try {
            sut.remove();
            fail("NoSuchElementException expected.");
        } catch (NoSuchElementException e) {
            assertTrue(true);
        }
    }

    public void test_offer_containsAllItems() {
        final List<String> testNames = TEST_NAMES.subList(0, DEFAULT_TEST_SIZE);
        for (String item : testNames) {
            sut.offer(item);
        }
        assertTrue(sut.containsAll(testNames));
    }

    public void test_priorityOrder_lowestFirst() {

        // build data
        final List<Integer> values = getSymmetricIntegers();
        // add it to the queue
        final Queue<Integer> priorityQueue = new ArrayPriorityQueue<>(values);
        // verify lowest items appear first
        Integer previous = 0;
        for (int i = 0; i < DEFAULT_TEST_SIZE; i++) {
            final Integer current = priorityQueue.poll();
            assertTrue(current > previous);
            previous = current;
        }
    }

    private List<Integer> getSymmetricIntegers() {
        final List<Integer> values = new ArrayList<>();
        for (int i = DEFAULT_TEST_SIZE; i > 0; i--) {
            values.add(values.size() >> 1, i);
        }
        return values;
    }
}
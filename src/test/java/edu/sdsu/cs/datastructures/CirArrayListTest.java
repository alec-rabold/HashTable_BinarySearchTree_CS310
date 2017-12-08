package edu.sdsu.cs.datastructures;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Unit tests modified in class ...
 */
public class CirArrayListTest extends TestCase {

    private static final int TEST_SIZE = 2048;
    private static final int VALUE_INVALID = -1;
    private static final int VALUE_VALID = 310;
    private static final int VALUE_IGNORE = 5555;

    private List<Integer> sut;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public CirArrayListTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(CirArrayListTest.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        sut = new CirArrayList<>();
    }

    public void test_constructorDefault_initializedCorrectly() {
        assertThat(sut.size(), is(equalTo(0)));
    }

    public void test_constructorCollection_initializedCorrectly() {

        final Integer[] values = getSequentialIntArray(TEST_SIZE);
        sut = new CirArrayList<>(Arrays.asList(values));

        assertThat(sut.size(), is(equalTo(values.length)));
        assertTrue(sut.containsAll(Arrays.asList(values)));
    }

    private Integer[] getSequentialIntArray(int size) {
        Integer[] values = new Integer[size];
        for (int i = 0; i < size; i++)
            values[i] = i;

        return values;
    }

    public void test_add_startEmptyEndPosition_contentsCorrect() {
        Integer[] values = getSequentialIntArray(TEST_SIZE);
        for (int position = 0; position < values.length; position++) {
            sut.add(position, values[position]);
        }

        assertThat(sut.size(), is(equalTo(values.length)));
        for (int position = 0; position < values.length; position++) {
            assertThat(sut.get(position), is(equalTo(values[position])));
        }
    }

    public void test_add_startEmptyFrontPosition_contentsCorrect() {
        Integer[] values = getSequentialIntArray(TEST_SIZE);
        for (int position = 0; position < values.length; position++) {
            sut.add(0, values[position]);
        }

        assertThat(sut.size(), is(equalTo(values.length)));
        Arrays.sort(values, Comparator.reverseOrder());
        for (int position = 0; position < values.length; position++) {
            assertThat(sut.get(position), is(equalTo(values[position])));
        }
    }

    public void test_add_existingCollectionFrontPosition_contentsCorrect() {
        Integer[] values = getSequentialIntArray(TEST_SIZE);
        sut = new CirArrayList(Arrays.asList(values));
        for (int value = TEST_SIZE - 1; value >= 0; value--) {
            sut.add(0, value);
        }

        assertThat(sut.size(), is(equalTo(TEST_SIZE << 1)));
        for (int position = 0; position < TEST_SIZE; position++) {
            assertThat(sut.get(position), is(equalTo(position)));
            assertThat(sut.get(position + TEST_SIZE), is(equalTo(position)));
        }
    }

    public void test_add_manySingleItems_correctContents() {

        final Integer inv = VALUE_INVALID;

        sut.add(VALUE_INVALID);
        for (int i = 0; i < Integer.MAX_VALUE >> 6; i++) {
            sut.add(inv);
            sut.remove(0);
        }
        sut.remove(0);
        sut.add(VALUE_VALID);

        assertEquals(sut.size(), 1);
        assertTrue(sut.contains(VALUE_VALID));
        assertFalse(sut.contains(VALUE_INVALID));
    }

    public void test_remove_addTwoBackRemoveOneFront_contentsCorrect() {
        int counter = 0;
        for (int cycle = 0; cycle < TEST_SIZE; cycle++) {
            sut.add(counter++);
            sut.add(counter++);
            sut.remove(0);
        }

        assertThat(sut.size(), is(equalTo(TEST_SIZE)));
        for (int i = 0; i < TEST_SIZE; i++) {
            assertFalse(sut.contains(i));
            assertThat(sut.get(i), is(equalTo(TEST_SIZE + i)));
        }
    }

    public void test_remove_allContents_correctRemovedElementAndSize() {
        sut = new CirArrayList<>(Arrays.asList(getSequentialIntArray(TEST_SIZE)));

        for (int count = 0; count < TEST_SIZE; count++) {
            assertThat(sut.remove(0), is(equalTo(count)));
            assertFalse(sut.contains(count));
            assertThat(sut.size(), is(equalTo(TEST_SIZE - count - 1)));
        }
    }

    public void test_set_sequentialValues_contentsCorrect() {
        sut = new CirArrayList<>(Arrays.asList(getInvalidInitializedIntArray(TEST_SIZE)));
        for (int count = 0; count < TEST_SIZE; count++) {
            sut.set(count, count);
        }

        assertEquals(sut.size(), TEST_SIZE);
        for (int count = 0; count < TEST_SIZE; count++) {
            assertThat(sut.get(count), is(equalTo(count)));
        }
    }

    private Integer[] getInvalidInitializedIntArray(int size) {
        Integer[] values = new Integer[size];
        for (int i = 0; i < size; i++)
            values[i] = VALUE_INVALID;

        return values;
    }

    public void test_remove_oddValuesFromList_nonePresent() {
        sut = new CirArrayList<>(Arrays.asList(getSequentialIntArray(TEST_SIZE)));

        sut.removeIf(integer -> integer % 2 == 1);

        for (int i = 0; i < TEST_SIZE; i += 2) {
            assertTrue(sut.contains(i));
            assertFalse(sut.contains(i + 1));
        }
    }

    public void test_clear_middleLeavingEnds_onlyValidItems() {
        sut = new CirArrayList<>(Arrays.asList(getInvalidInitializedIntArray(TEST_SIZE)));
        sut.set(0, VALUE_VALID);
        sut.set(sut.size() - 1, VALUE_VALID);

        assertTrue(sut.contains(VALUE_INVALID));
        sut.subList(1, sut.size() - 1).clear();
        assertFalse(sut.contains(VALUE_INVALID));
        assertEquals(sut.size(), 2);
    }

    public void test_set_indexOutOfBounds_exceptionThrown() {
        try {
            sut.set(-1, VALUE_IGNORE);
            fail("IndexOutOfBoundsException expected for index < 0");
        } catch (IndexOutOfBoundsException e) {

        } catch (Exception e) {
            fail("Incorrect Exception");
        }

        try {
            sut.set(0, VALUE_IGNORE);
            fail("IndexOutOfBoundsException expected for == size()");
        } catch (IndexOutOfBoundsException e) {

        } catch (Exception e) {
            fail("Incorrect Exception");
        }

        try {
            sut.set(1, VALUE_IGNORE);
            fail("IndexOutOfBoundsException expected for > size()");
        } catch (IndexOutOfBoundsException e) {

        } catch (Exception e) {
            fail("Incorrect Exception");
        }

        assertTrue(true);
    }

    public void test_get_indexOutOfBounds_exceptionThrown() {
        try {
            sut.get(-1);
            fail("IndexOutOfBoundsException expected for < 0");
        } catch (IndexOutOfBoundsException e) {

        } catch (Exception e) {
            fail("Incorrect Exception");
        }

        try {
            sut.get(0);
            fail("IndexOutOfBoundsException expected for == size()");
        } catch (IndexOutOfBoundsException e) {

        } catch (Exception e) {
            fail("Incorrect Exception");
        }

        try {
            sut.get(1);
            fail("IndexOutOfBoundsException expected for > size()");
        } catch (IndexOutOfBoundsException e) {

        } catch (Exception e) {
            fail("Incorrect Exception");
        }

        assertTrue(true);

    }
}

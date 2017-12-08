package edu.sdsu.cs.util;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A selection of methods used to performance test on any data structure
 * implementing the java.util.List interface.
 *
 * @author Shawn Healey, San Diego State University
 * @version 1.0
 */
public final class ListTimer {

    private static final long DEFAULT_START_SIZE = 1024;

    /**
     * Used when establishing the starting size N to use for the complexity test
     */
    private static final long MINIMUM_TEST_TIME = 8000000;

    /**
     * To illustrate complexity growth, one must show how the problem slows
     * down as the input scales. This number represents the number of
     * times the input size doubles during the tests.
     */
    private static final long DEFAULT_NUM_EPOCHS = 5;

    /**
     * Performs a basic set of timing tests on the List ADT.
     *
     * @param sut    Any data structure implementing the java.util.List
     *               interface
     * @param values The object to use when populating test data
     * @param <E>    What types of values go in the list (Integers, Strings?)
     * @return A list, ready for writing, with the output results
     */
    public static <E> List<String> timeList(List<E> sut, IValueGenerator<E>
            values) {

        long startingN = establishStartingSize(sut, values);

        List<String> output = new LinkedList<>();
        output.add(OutputStrings.getTimeTag());
        output.addAll(timeAddMethods(sut, values, startingN));
        output.addAll(timeClearMethods(sut, values, startingN));
        output.addAll(timeSetMethods(sut, values, startingN));
        output.addAll(timeRemoveMethods(sut, values, startingN));
        output.add(OutputStrings.getTimeTag());
        return output;
    }

    private static <E> long establishStartingSize(List<E> sut,
                                                  IValueGenerator<E> values) {
        long curSize = DEFAULT_START_SIZE >> 1;
        long time = -1;
        while (time < MINIMUM_TEST_TIME) {
            curSize <<= 1;
            time = timeOperation(new AddMiddleCommand<>(sut, values), curSize);
        }
        sut.clear();
        return curSize;
    }

    private static <E1> List<String> timeAddMethods(List<E1> sut,
                                                    IValueGenerator<E1>
                                                            values, long
                                                            endingSize) {
        List<String> output = new java.util.LinkedList<>();

        writeSeparator(output, String.format("%s: add( E )", sut.getClass()));
        test(null, 0, new AddCommand<>(sut, values, false), endingSize, new
                ClearCommand<>(sut), output);
        writeSeparator(output, String.format("%s: add( middle, E )", sut
                .getClass()));
        test(null, 0, new AddMiddleCommand<>(sut, values), endingSize, new
                ClearCommand<>(sut), output);
        writeSeparator(output, String.format("%s: add( 0, E )", sut.getClass
                ()));
        test(null, 0, new AddCommand<>(sut, values, true), endingSize, new
                ClearCommand<>(sut), output);
        return output;
    }

    private static <E1> List<String> timeClearMethods(List<E1> sut,
                                                      IValueGenerator<E1>
                                                              values, long
                                                              endingSize) {
        List<String> output = new LinkedList<>();

        writeSeparator(output, String.format("%s: clear( )", sut.getClass()));
        test(new AddCommand<>(sut, values, false), endingSize, new
                ClearCommand<>(sut), 1, null, output);

        return output;
    }

    private static <E1> List<String> timeSetMethods(List<E1> sut,
                                                    IValueGenerator<E1>
                                                            values, long
                                                            endingSize) {
        List<String> output = new LinkedList<>();
        writeSeparator(output, String.format("%s: set( i->n )", sut.getClass
                ()));
        test(new AddCommand<>(sut, values, false), endingSize, new
                SetCommand<>(sut, new IValueGenerator<E1>() {
            final E1 name = values.generate();

            @Override
            public E1 generate() {
                return name;
            }
        }), endingSize, new ClearCommand<>(sut), output);

        return output;
    }

    private static <E1> List<String> timeRemoveMethods(List<E1> sut,
                                                       IValueGenerator<E1>
                                                               values, long
                                                               endingSize) {
        List<String> output = new java.util.LinkedList<>();

        writeSeparator(output, String.format("%s: remove( <middle> )", sut
                .getClass()));

        // todo: BEGIN STUDENT CODE SECTION
        output.add("REMOVE THIS AND REPLACE WITH CORRECT RESULTS");
        // todo: END STUDENT CODE SECTION

        writeSeparator(output, String.format("%s: remove( 0 )", sut.getClass
                ()));
        test(new AddCommand<>(sut, values, false), endingSize, new
                RemoveFrontCommand<>(sut), endingSize, null, output);

        writeSeparator(output, String.format("%s: remove( <last> )", sut
                .getClass()));
        // todo: BEGIN STUDENT CODE SECTION
        output.add("REMOVE THIS AND REPLACE WITH CORRECT RESULTS");
        // todo: END STUDENT CODE SECTION
        return output;
    }

    private static long timeOperation(ICommand cmd, long numOps) {
        long startTime = System.nanoTime();
        for (long count = 0; count < numOps; count++) {
            cmd.execute();
        }
        return System.nanoTime() - startTime;
    }

    private static void writeSeparator(List<String> output, String title) {
        output.add(OutputStrings.simpleDivider());
        output.add(OutputStrings.titleDivider(title));
        output.add(OutputStrings.simpleDivider());
    }

    private static void test(ICommand setupCommand, long setupSize, ICommand
            cmd, long cmdSize, ICommand cleanup, List<String> output) {

        for (int epoch = 0; epoch < DEFAULT_NUM_EPOCHS; epoch++) {

            for (int i = 0; i < setupSize << epoch; i++) {
                setupCommand.execute();
            }

            long startTime = System.nanoTime();
            for (int i = 0; i < cmdSize << epoch; i++) {
                cmd.execute();
            }
            output.add(String.format("Size %07d(%07d): %8d mS", cmdSize <<
                    epoch, setupSize << epoch, TimeUnit.NANOSECONDS.toMillis
                    (System.nanoTime() - startTime)));

            if (cleanup != null) cleanup.execute();
        }
    }

    private abstract static class ListCommand<E1> implements ICommand {
        final List<E1> sut;
        final IValueGenerator<E1> valueGenerator;

        ListCommand(List<E1> underTest, IValueGenerator<E1> values) {
            sut = underTest;
            valueGenerator = values;
        }
    }

    private static class AddCommand<E1> extends ListCommand<E1> {
        private boolean addToFront;

        AddCommand(List<E1> underTest, IValueGenerator<E1> values, boolean
                toFront) {
            super(underTest, values);
            addToFront = toFront;
        }

        @Override
        public void execute() {
            if (addToFront) {
                sut.add(0, valueGenerator.generate());
            } else {
                sut.add(valueGenerator.generate());
            }
        }
    }

    private static class AddMiddleCommand<E1> extends ListCommand<E1> {
        AddMiddleCommand(List<E1> underTest, IValueGenerator<E1> values) {
            super(underTest, values);
        }

        @Override
        public void execute() {
            sut.add(sut.size() >> 1, valueGenerator.generate());
        }
    }

    private static class ClearCommand<E1> extends ListCommand<E1> {
        ClearCommand(List<E1> underTest) {
            super(underTest, null);
        }

        @Override
        public void execute() {
            sut.clear();
        }
    }

    private static class RemoveFrontCommand<E1> extends ListCommand<E1> {
        RemoveFrontCommand(List<E1> underTest) {
            super(underTest, null);
        }

        @Override
        public void execute() {
            sut.remove(0);
        }
    }

    private static final class SetCommand<E1> extends ListCommand<E1> {

        int cursor = 0;

        SetCommand(List<E1> toTest, IValueGenerator<E1> values) {
            super(toTest, values);
        }

        @Override
        public void execute() {
            if (cursor >= sut.size()) cursor = 0;
            sut.set(cursor++, valueGenerator.generate());
        }
    }
}

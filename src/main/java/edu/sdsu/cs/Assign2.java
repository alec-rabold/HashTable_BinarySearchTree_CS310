package edu.sdsu.cs;

import edu.sdsu.cs.datastructures.CirArrayList;
import edu.sdsu.cs.util.IValueGenerator;
import edu.sdsu.cs.util.ListTimer;
import edu.sdsu.cs.util.NameGenerator;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Assignment 2: Lists and inheritance
 * <p>The requirements defined on the class
 * <a href="https://edoras.sdsu.edu/~healey/cs310>website</a> define the
 * requirements for this project.</p>
 *
 * @author STUDENT NAME HERE, csscXXXX
 */
public class Assign2 {

    private static final String TIMING_CAL_FILE = "timing_cal.txt";

    private Assign2() {
        final IValueGenerator<String> values = new NameGenerator();

        writeFile(ListTimer.timeList(new CirArrayList<>(), values),
                TIMING_CAL_FILE);
    }

    private static void writeFile(List<String> data, String filename) {
        try {
            Files.write(Paths.get(filename), data, Charset.defaultCharset());
        } catch (SecurityException se) {
            System.err.println("Security Exception : " + se.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Application entry point which simply starts the security manager and
     * then creates an instance of the host class.
     *
     * @param args No runtime args
     */
    public static void main(String[] args) {

        try {
            new Assign2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

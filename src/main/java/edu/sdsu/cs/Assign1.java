package edu.sdsu.cs;

import edu.sdsu.cs.util.Fingerprint;
import edu.sdsu.cs.util.IValueGenerator;
import edu.sdsu.cs.util.ListTimer;
import edu.sdsu.cs.util.NameGenerator;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Assignment 1: Setup and Complexity
 * <p>The requirements defined on the class
 * <a href="https://edoras.sdsu.edu/~healey/cs310>website</a> define the
 * requirements for this project.</p>
 *
 * @author STUDENT NAME HERE, csscXXXX
 */
public final class Assign1 {

    private static final String SECURITY_POLICY = "file:./example.policy";

    private static final String FINGERPRINT_FILE = "fingerprint.txt";
    private static final String TIMING_AL_FILE = "timing_al.txt";
    private static final String TIMING_LL_FILE = "timing_ll.txt";

    private static final String NAMES_FAMILY =
            "top_1000_usa_familynames_english.txt";

    private static final String NAMES_FIRST_F =
            "top_1000_usa_femalenames_english.txt";

    private static final String NAMES_FIRST_M =
            "top_1000_usa_malenames_english.txt";

    /**
     * Directs the majority of the work in the application.
     */
    private Assign1() {

        final IValueGenerator<String> values = new NameGenerator
                (NAMES_FAMILY, NAMES_FIRST_F, NAMES_FIRST_M);

        // Run each thread
        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.submit(new Thread(Assign1::writeFingerprint));
        Future<List<String>> results = executor.submit(() -> ListTimer
                .timeList(new ArrayList<>(), values));

        // todo: Students add code to test a LinkedList using the same technique

        try {
            writeFile(results.get(), TIMING_AL_FILE);
            // todo: Students must write output from LL future

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        executor.shutdown();
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

    private static void writeFingerprint() {
        writeFile(Fingerprint.getFingerprint(), FINGERPRINT_FILE);
    }

    /**
     * Application entry point which simply starts the security manager and
     * then creates an instance of the host class.
     *
     * @param args No runtime args
     */
    public static void main(String[] args) {

        startSecurityManager(SECURITY_POLICY);
        try {
            new Assign1();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startSecurityManager(String policy) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager == null) {
            System.setProperty("java.security.policy", policy);
            System.setSecurityManager(new SecurityManager());
        }
    }
}


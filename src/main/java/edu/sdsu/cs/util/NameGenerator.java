package edu.sdsu.cs.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Random;

/**
 * Creates names based on a combination of the files provided.
 *
 * @author Shawn Healey, San Diego State Univeristy
 * @version 1.0
 */
public final class NameGenerator implements IValueGenerator<String> {

    private static final String NAMES_FAMILY =
            "top_1000_usa_familynames_english.txt";

    private static final String NAMES_FIRST_F =
            "top_1000_usa_femalenames_english.txt";

    private static final String NAMES_FIRST_M =
            "top_1000_usa_malenames_english.txt";

    /**
     * Initialize the random number generator for this generator.
     */
    private final Random dice = new Random(310);

    private final List<String> lastNames;
    private final List<String> firstNames;

    /**
     * Uses the built-in default file names to construct a name generator.
     */
    public NameGenerator() {
        this(NAMES_FAMILY, NAMES_FIRST_F, NAMES_FIRST_M);
    }

    /**
     * Creates a name generation object of the form LAST, FIRST
     *
     * @param lastNameFile   The name of the resource containing the last names
     *                       to use when generating names with this object.
     * @param firstNameFiles Any number of files to use as valid first names.
     */
    public NameGenerator(String lastNameFile, String... firstNameFiles) {
        lastNames = new java.util.ArrayList<>();
        firstNames = new java.util.ArrayList<>();

        addNamesToListFromFile(lastNameFile, lastNames);
        for (String fileName : firstNameFiles) {
            addNamesToListFromFile(fileName, firstNames);
        }
    }

    private void addNamesToListFromFile(String fileName, List<String> list) {
        URL dataFile = getClass().getClassLoader().getResource(fileName);
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader
                    (dataFile.openStream()));
            String name = br.readLine();
            while (name != null) {
                list.add(name);
                name = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String generate() {
        return lastNames.get(dice.nextInt(lastNames.size())) + ", " +
                firstNames.get(dice.nextInt(firstNames.size()));
    }
}

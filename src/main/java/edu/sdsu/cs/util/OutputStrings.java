package edu.sdsu.cs.util;

import java.nio.CharBuffer;
import java.time.LocalDateTime;

/**
 * Tools for generating ASCII strings for human-readable output.
 *
 * @author Shawn Healey, San Diego State University
 * @version 1.0
 */
final class OutputStrings {

    /**
     * Establishes the default column width used when printing out dividers
     * and repeating characters.
     */
    private static final int DEFAULT_COL_WIDTH = 80;

    /**
     * The default character used when producing ASCII dividers.
     */
    private static final char DEFAULT_DIVIDER_CHAR = '*';

    /**
     * Produces an ASCII character divider using the default width and divider
     * character.
     *
     * @return A string of the default character ('*') repeated the default
     * width (80) times.
     */
    public static String simpleDivider() {
        return simpleDivider(DEFAULT_COL_WIDTH);
    }

    /**
     * Produces an ASCII character divider using the default divider character.
     *
     * @param width number of characters to print
     * @return A string of the default character ('*') repeated width times
     */
    public static String simpleDivider(int width) {
        return repeatChar(width, DEFAULT_DIVIDER_CHAR);
    }

    /**
     * Simple helper method to generate strings initialized with the provided
     * symbol.
     *
     * @param count  width of string
     * @param symbol character to repeat
     * @return A string of width count initialized with the parameter symbol
     * @implNote  https://stackoverflow
     * .com/questions/2804827/create-a-string-with-n-characters
     */
    public static String repeatChar(int count, char symbol) {
        return CharBuffer.allocate(count).toString().replace('\0', symbol);
    }

    /**
     * Produces an ASCII divider of the default width, using the default
     * character, with the provided title centered.
     *
     * @param title
     * @return A character divider with the appropriate title
     */
    public static String titleDivider(String title) {
        int width = OutputStrings.DEFAULT_COL_WIDTH;
        if (title != null) {
            width -= title.length() + 2;
        }
        width >>= 1;
        StringBuilder sb = new StringBuilder(OutputStrings.repeatChar(width,
                ' '));
        if (title != null) {
            sb.append(" " + title + " ");
            if (title.length() % 2 == 1) sb.append(" ");
        }
        return sb.toString();
    }

    /**
     * Supplies the caller with a simple USER: Time time tag
     *
     * @return String with the user name and time tag
     */
    public static String getTimeTag() {
        return String.format("%s:%s", System.getProperty("user.name", "not "
                + "specified"), LocalDateTime.now());
    }
}

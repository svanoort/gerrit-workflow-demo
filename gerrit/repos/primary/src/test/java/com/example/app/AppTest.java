package com.example.app;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;

/**
 * Basic unit tests for command generation
 */
public class AppTest {

    @Test
    public void testRandomStrings() throws Exception {
        String chars = "ABCDEDF";
        String output = App.randomString(chars, 10);
        assertEquals(10, output.length());

        // Check output
        HashSet<Character> validChars = new HashSet<Character>();
        for (char v : chars.toCharArray()) {
            validChars.add(v);
        }

        for (char v : output.toCharArray()) {
            assertTrue(validChars.contains(new Character(v)));
        }
    }
}

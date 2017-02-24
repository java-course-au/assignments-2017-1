package ru.spbau.mit;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringSetTest {
    private StringSet stringSet;

    @Before
    public void setUp() throws Exception {
        stringSet = new StringSetImpl();
    }

    @Test
    public void testAddOneString() throws Exception {
        assertTrue(stringSet.add("string"));
        assertTrue(stringSet.contains("string"));
        assertEquals(1, stringSet.size());
    }

    @Test
    public void testAddTwoStrings() throws Exception {
        assertTrue(stringSet.add("firstString"));
        assertTrue(stringSet.add("secondString"));
        assertTrue(stringSet.contains("firstString"));
        assertTrue(stringSet.contains("secondString"));
        assertEquals(2, stringSet.size());
    }

    @Test
    public void testAddOneStringTwice() throws Exception {
        assertTrue(stringSet.add("string"));
        assertFalse(stringSet.add("string"));
        assertTrue(stringSet.contains("string"));
        assertEquals(1, stringSet.size());
    }

    @Test
    public void testContains() throws Exception {
        assertFalse(stringSet.contains("firstString"));
        stringSet.add("firstString");
        assertTrue(stringSet.contains("firstString"));
        assertFalse(stringSet.contains("secondString"));
    }

    @Test
    public void testRemove() throws Exception {
        assertFalse(stringSet.remove("string"));
        stringSet.add("string");
        assertTrue(stringSet.remove("string"));
        assertFalse(stringSet.contains("string"));
    }

    @Test
    public void testSize() throws Exception {
        assertEquals(0, stringSet.size());

        stringSet.add("firstString");
        assertEquals(1, stringSet.size());
        stringSet.add("secondString");
        assertEquals(2, stringSet.size());
        stringSet.add("secondString");
        assertEquals(2, stringSet.size());

        stringSet.remove("secondString");
        assertEquals(1, stringSet.size());
        stringSet.remove("secondString");
        assertEquals(1, stringSet.size());
        stringSet.remove("firstString");
        assertEquals(0, stringSet.size());
    }

    @Test
    public void testHowManyStartsWithPrefix() throws Exception {
        stringSet.add("a");
        stringSet.add("aa");
        stringSet.add("ab");
        stringSet.add("aaa");

        assertEquals(stringSet.size(), stringSet.howManyStartsWithPrefix(""));
        assertEquals(stringSet.size(), stringSet.howManyStartsWithPrefix("a"));
        assertEquals(2, stringSet.howManyStartsWithPrefix("aa"));
        assertEquals(1, stringSet.howManyStartsWithPrefix("ab"));
        assertEquals(1, stringSet.howManyStartsWithPrefix("aaa"));
        assertEquals(0, stringSet.howManyStartsWithPrefix("aba"));
    }

    @Test
    public void testEmptyString() throws Exception {
        assertFalse(stringSet.contains(""));
        assertTrue(stringSet.add(""));
        assertTrue(stringSet.contains(""));
        assertEquals(1, stringSet.size());
        assertTrue(stringSet.remove(""));
        assertFalse(stringSet.contains(""));
        assertEquals(0, stringSet.size());
    }

    @Test
    public void testLegalCharactersInString() throws Exception {
        final String lowerCaseLatin = "abcdefghijklmnopqrstuvwxyz";
        stringSet.add(lowerCaseLatin);

        final String upperCaseLatin = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        stringSet.add(upperCaseLatin);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalCharactersInString() throws Exception {
        final String illegalCharacters = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
        stringSet.add(illegalCharacters);
    }
}

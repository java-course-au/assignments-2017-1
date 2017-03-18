package ru.spbau.mit;

import static org.junit.Assert.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StringSetTest {

    @Test
    public void testSimple() {
        StringSet stringSet = instance();

        assertTrue(stringSet.add("abc"));
        assertTrue(stringSet.contains("abc"));
        assertEquals(1, stringSet.size());
        assertEquals(1, stringSet.howManyStartsWithPrefix("abc"));
    }

    @Test
    public void testDoubleAdd() {
        StringSet stringSet = instance();

        assertTrue(stringSet.add("abc"));
        assertFalse(stringSet.add("abc"));
        assertEquals(1, stringSet.size());
        assertEquals(1, stringSet.howManyStartsWithPrefix("abc"));
    }

    @Test
    public void testDoubleRemove() {
        StringSet stringSet = instance();

        assertTrue(stringSet.add("abc"));
        assertTrue(stringSet.remove("abc"));
        assertEquals(0, stringSet.size());
        assertEquals(0, stringSet.howManyStartsWithPrefix("abc"));
    }

    @Test
    public void testEmpty() {
        StringSet stringSet = instance();

        assertFalse(stringSet.remove(""));
        assertFalse(stringSet.contains(""));
        assertEquals(0, stringSet.howManyStartsWithPrefix(""));
        assertEquals(0, stringSet.size());

        assertTrue(stringSet.add("abc"));
        assertFalse(stringSet.contains(""));
        assertEquals(1, stringSet.size());

        assertTrue(stringSet.add(""));
        assertTrue(stringSet.contains(""));
        assertEquals(2, stringSet.size());

        assertTrue(stringSet.remove(""));
        assertFalse(stringSet.contains(""));
        assertEquals(1, stringSet.size());
    }

    @Test
    public void testPrefix() {
        StringSet stringSet = instance();

        assertTrue(stringSet.add("abcd"));
        assertTrue(stringSet.contains("abcd"));
        assertFalse(stringSet.contains("abc"));
    }

    @Test
    public void testPrefixRemove() {
        StringSet stringSet = instance();

        assertTrue(stringSet.add("abcd"));
        assertTrue(stringSet.add("abc"));
        assertTrue(stringSet.remove("abcd"));

        assertFalse(stringSet.contains("abcd"));
        assertTrue(stringSet.contains("abc"));
        assertEquals(1, stringSet.size());
    }

    @Test
    public void testOverlapped() {
        StringSet stringSet = instance();

        assertTrue(stringSet.add("abcd"));
        assertTrue(stringSet.add("abce"));
        assertFalse(stringSet.contains("abc"));

        assertEquals(2, stringSet.size());
        assertEquals(2, stringSet.howManyStartsWithPrefix("abc"));
        assertEquals(1, stringSet.howManyStartsWithPrefix("abce"));
        assertEquals(0, stringSet.howManyStartsWithPrefix("abcm"));
    }

    @Test
    public void testRemove() {
        StringSet stringSet = instance();

        assertTrue(stringSet.add("abc"));
        assertTrue(stringSet.add("dbc"));

        assertFalse(stringSet.remove(""));
        assertFalse(stringSet.remove("ab"));
        assertFalse(stringSet.remove("abce"));
        assertEquals(2, stringSet.size());
        assertTrue(stringSet.contains("abc"));
        assertTrue(stringSet.contains("dbc"));

        assertTrue(stringSet.remove("abc"));
        assertEquals(1, stringSet.size());
        assertFalse(stringSet.contains("abc"));
        assertTrue(stringSet.contains("dbc"));
    }


    public static StringSet instance() {
        try {
            return (StringSet) Class.forName("ru.spbau.mit.StringSetImpl").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("Error while class loading");
    }
}

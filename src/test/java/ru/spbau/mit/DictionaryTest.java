package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.*;

public class DictionaryTest {
    @Test
    public void testSimplePutGet() {
        Dictionary dict = instance();
        assertNull(dict.get("abc"));

        assertNull(dict.put("abc", "cde"));
        assertEquals("cde", dict.get("abc"));
    }

    @Test
    public void testPutAndClear() {
        Dictionary dict = instance();
        final int dictFinalSize = 30000;

        for (int i = 0; i < dictFinalSize; i++) {
            assertNull(dict.put(Integer.toString(i), null));
        }

        assertEquals(dictFinalSize, dict.size());
        dict.clear();
        assertEquals(0, dict.size());
        assertNull(dict.get(Integer.toString(dictFinalSize - 1)));
    }

    @Test
    public void testContains() {
        Dictionary dict = instance();
        String testSting = "test";

        assertFalse(dict.contains(testSting));
        assertNull(dict.put(testSting, testSting));
        assertTrue(dict.contains(testSting));
    }

    @Test
    public void testRemove() {
        Dictionary dict = instance();
        String testString = "test";

        assertNull(dict.remove(testString));
        assertNull(dict.put(testString, testString));
        assertTrue(dict.contains(testString));
        assertEquals(testString, dict.remove(testString));
        assertFalse(dict.contains(testString));
        assertEquals(0, dict.size());
    }

    @Test
    public void testPutAndRemove() {
        Dictionary dict = instance();
        final int testsNum = 30000;

        for (int i = 0; i < testsNum; i++) {
            assertNull(dict.put(Integer.toString(i), Integer.toString(i)));
            assertEquals(i + 1, dict.size());
            assertEquals(Integer.toString(i), dict.get(Integer.toString(i)));
        }

        int size = testsNum;

        for (int i = 0; i < testsNum; i += 2) {
            assertEquals(Integer.toString(i), dict.remove(Integer.toString(i)));
            assertNull(dict.remove(Integer.toString(i)));
            size--;
            assertEquals(size, dict.size());
        }

        assertEquals(testsNum / 2, dict.size());
    }

    @Test
    public void testAddJustRemoved() {
        Dictionary dict = instance();

        assertNull(dict.put("Test String Key", "Test String Value"));
        assertEquals("Test String Value", dict.remove("Test String Key"));
        assertNull(dict.put("Test String Key", "Test String Value"));
    }

    @Test
    public void testUpdateValue() {
        Dictionary dict = instance();

        assertNull(dict.put("first", "first value"));
        assertEquals("first value", dict.put("first", "second value"));
        assertEquals("second value", dict.get("first"));
        assertEquals(1, dict.size());
    }

    private static Dictionary instance() {
        try {
            return (Dictionary) Class.forName("ru.spbau.mit.DictionaryImpl").newInstance();
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

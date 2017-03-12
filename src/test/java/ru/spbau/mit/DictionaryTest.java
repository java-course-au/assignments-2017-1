package ru.spbau.mit;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DictionaryTest {
    private static final int SIZETEST_STRING_NUM = 3;
    private Dictionary dict;

    @Before
    public void setup() {
        dict = instance();
    }

    @Test
    public void testSimplePutGet() {
        assertNull(dict.get("abc"));

        assertNull(dict.put("abc", "cde"));
        assertEquals("cde", dict.get("abc"));
    }

    @Test
    public void testNotExistingKey() {
        assertNull(dict.get(""));
    }

    @Test
    public void testReplace() {
        assertNull(dict.put("key", "value1"));
        assertEquals("value1", dict.put("key", "value1"));
        assertEquals("value1", dict.put("key", "value2"));
        assertEquals("value2", dict.get("key"));
    }

    @Test
    public void testRemove() {
        assertNull(dict.put("abc", "cde1"));
        assertNull(dict.put("abc1", "cde2"));
        assertEquals("cde1", dict.remove("abc"));
        assertEquals("cde2", dict.get("abc1"));
    }

    @Test
    public void testFalseRemove() {
        assertNull(dict.remove("111"));
        assertEquals(0, dict.size());
    }

    @Test
    public void testDoubleRemove() {
        assertNull(dict.put("abc", "cde1"));
        assertEquals("cde1", dict.remove("abc"));
        assertNull(dict.remove("abc"));
        assertEquals(0, dict.size());
    }

    @Test
    public void testContains() {
        assertNull(dict.put("abc", "cde1"));
        assertNull(dict.put("efg", "cde2"));
        assertTrue(dict.contains("abc"));
        assertTrue(dict.contains("efg"));
        assertFalse(dict.contains("efg1"));
    }

    @Test
    public void testClear() {
        assertNull(dict.put("abc", "cde1"));
        assertNull(dict.put("efg", "cde2"));
        dict.clear();

        assertFalse(dict.contains("abc"));
        assertFalse(dict.contains("efg"));
        assertEquals(0, dict.size());
    }


    @Test
    public void testSize() {
        assertEquals(dict.size(), 0);

        dict.put("abc", "cde");
        dict.put("abc1", "cde");
        dict.put("abc", "cde");
        dict.put("abdc", "cde");
        assertEquals(SIZETEST_STRING_NUM, dict.size());

        dict.remove("abc");
        assertEquals(SIZETEST_STRING_NUM - 1, dict.size());
    }

    @Test
    public void testEmptySize() {
        assertEquals(0, dict.size());
        assertFalse(dict.contains(""));
    }

    @Test
    public void testPutIncrementSize() {
        assertNull(dict.put("key1", "v1"));
        assertEquals(1, dict.size());

        assertNull(dict.put("key2", "v2"));
        assertEquals(2, dict.size());
    }

    @Test
    public void testDoublePut() {
        final String key = "key";
        assertNull(dict.put(key, "1"));
        assertEquals("1", dict.put(key, "2"));
        assertEquals(1, dict.size());
        assertEquals("2", dict.get(key));
    }

    @Test
    public void testOverlappedPut() {
        final Dictionary dict = instance();

        assertNull(dict.put("key", "value1"));
        assertEquals("value1", dict.put("key", "value2"));
        assertEquals("value2", dict.get("key"));
    }


    @Test
    public void testLong() {
        assertNull(dict.put("1", "val1"));
        assertNull(dict.put("2", "val2"));
        assertNull(dict.put("3", "val3"));
        assertNull(dict.put("4", "val4"));

        assertTrue(dict.contains("1"));
        assertTrue(dict.contains("2"));
        assertTrue(dict.contains("3"));
        assertTrue(dict.contains("4"));

        assertEquals("val1", dict.get("1"));
        assertEquals("val2", dict.get("2"));
        assertEquals("val3", dict.get("3"));
        assertEquals("val4", dict.get("4"));

        assertEquals("val1", dict.remove("1"));
        assertEquals("val4", dict.remove("4"));

        assertNull(dict.get("1"));
        assertEquals("val2", dict.get("2"));
        assertEquals("val3", dict.get("3"));
        assertNull(dict.get("4"));

        assertEquals("val2", dict.remove("2"));
        assertEquals("val3", dict.remove("3"));

        assertNull(dict.get("1"));
        assertNull(dict.get("4"));
        assertNull(dict.get("2"));
        assertNull(dict.get("3"));
    }

    @Test
    public void testNull() {
        assertNull(dict.put("1", null));
        assertEquals(null, dict.get("1"));
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

package ru.spbau.mit;

import org.junit.Test;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

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
    public void testDelete() {
        Dictionary dict = instance();
        assertNull(dict.put("abc", "cde"));
        assertEquals("cde", dict.remove("abc"));
        assertFalse(dict.contains("abc"));
    }

    @Test
    public void testContains() {
        Dictionary dict = instance();
        assertNull(dict.put("abc", "cde"));
        assertTrue(dict.contains("abc"));
    }

    @Test
    public void testSize() {
        Dictionary dict = instance();

        assertNull(dict.put("abc", "cde"));
        assertNull(dict.put("dfg", "hjk"));
        assertNull(dict.put("jkl", "iop"));
        assertEquals(dict.size(), 1 + 1 + 1);
        assertEquals("cde", dict.remove("abc"));
        assertEquals(dict.size(), 2);
    }

    @Test
    public void testDoubleDelete() {
        Dictionary dict = instance();
        assertNull(dict.put("abc", "cde"));
        assertEquals("cde", dict.remove("abc"));
        assertNull(dict.remove("abc"));
        assertFalse(dict.contains("abc"));
    }

    @Test
    public void testDoubleAdd() {
        Dictionary dict = instance();
        assertNull(dict.put("abc", "cde"));
        assertEquals("cde", dict.put("abc", "cde"));
        assertEquals(1, dict.size());
    }

    @Test
    public void testEmpty() {
        Dictionary dict = instance();
        assertEquals(0, dict.size());
        assertFalse(dict.contains("abc"));
        assertNull(dict.get("abc"));
        assertNull(dict.remove("abc"));
        assertEquals(0, dict.size());
    }

    @Test
    public void testClear() {
        Dictionary dict = instance();
        assertNull(dict.put("abc", "cde"));
        assertNull(dict.put("dfg", "hjk"));
        assertNull(dict.put("jkl", "iop"));
        assertTrue(dict.contains("abc"));
        assertTrue(dict.contains("dfg"));
        assertTrue(dict.contains("jkl"));
        dict.clear();
        assertEquals(0, dict.size());
        assertFalse(dict.contains("abc"));
        assertFalse(dict.contains("dfg"));
        assertFalse(dict.contains("jkl"));
    }

    @Test
    public void testRehashing() {
        Dictionary dict = instance();

        final int size = 1000000;

        for (int i = 0; i < size; i++) {
            assertNull(dict.put("k" + i, "v" + i));
        }

        for (int i = 0; i < size; i++) {
            assertEquals("v" + i, dict.get("k" + i));
        }

        assertEquals(size, dict.size());
    }

    @Test
    public void testRandom() {
        Dictionary dict = instance();
        Map<String, String> mapJdk = new HashMap<>();

        final int size = 10000;
        for (int i = 0; i < size; i++) {
            final int bitLength = 100;
            String randKey = new BigInteger(bitLength, new Random()).toString();
            String randValue = new BigInteger(bitLength, new Random()).toString();

            dict.put(randKey, randValue);
            mapJdk.put(randKey, randValue);
        }

        Set<String> keys = new HashSet<>(mapJdk.keySet());

        keys.forEach(key -> assertEquals(dict.get(key), mapJdk.get(key)));

        keys = keys
                .stream()
                .limit(size / (2 * 2 * 2 * 2)).collect(Collectors.toSet());

        keys.forEach(key -> assertEquals(dict.remove(key), mapJdk.remove(key)));

        keys.forEach(key -> assertFalse(dict.contains(key)));

        assertEquals(mapJdk.size(), dict.size());
    }

    @Test
    public void testNulls() {
        Dictionary dict = instance();
        dict.put(null, null);
        assertFalse(dict.contains(null));

        assertNull(dict.put("abc", null));
        assertTrue(dict.contains("abc"));
        assertEquals(1, dict.size());
        assertEquals(null, dict.get("abc"));

        dict.put("abc", null);
        assertEquals(1, dict.size());

        dict.remove("abc");
        assertFalse(dict.contains("abc"));

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

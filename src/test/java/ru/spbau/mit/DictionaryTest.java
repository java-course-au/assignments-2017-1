package ru.spbau.mit;

import org.junit.Test;

import java.util.UUID;

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
    public void crashTest() {
        Dictionary dict = instance();
        assertNull(dict.get("abc"));

        String[] keys = new String[500];
        String[] values = new String[500];
        for (int i = 0; i < 500; i++) {
            String key = String.valueOf(i);
            String value = UUID.randomUUID().toString();
            keys[i] = key;
            values[i] = value;
            assertEquals(dict.size(), i);
            assertNull(dict.put(key, value));
        }

        for (int i = 0; i < 500; i++) {
            assertEquals(values[i], dict.get(keys[i]));
        }

        for (int i = 0; i < 500; i++) {
            dict.remove(keys[i]);
            assertFalse(dict.contains(keys[i]));
            assertEquals(499 - i, dict.size());
        }

        assertNull(dict.put("abc", "cde"));
        assertEquals("cde", dict.get("abc"));
    }

    @Test
    public void testClear() {
        Dictionary dict = instance();
        assertNull(dict.get("abc"));

        for (int i = 0; i < 500; i++) {
            String key = String.valueOf(i);
            assertNull(dict.put(key, key));
        }

        dict.clear();

        for (int i = 0; i < 500; i++) {
            assertFalse(dict.contains(String.valueOf(i)));
            assertNull(dict.get(String.valueOf(i)));
        }

        assertNull(dict.put("abc", "cde"));
        assertEquals("cde", dict.get("abc"));
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


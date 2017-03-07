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

        final int elementsSize = 500;
        String[] keys = new String[elementsSize];
        String[] values = new String[elementsSize];
        for (int i = 0; i < elementsSize; i++) {
            String key = String.valueOf(i);
            String value = UUID.randomUUID().toString();
            keys[i] = key;
            values[i] = value;
            assertEquals(dict.size(), i);
            assertNull(dict.put(key, value));
        }

        for (int i = 0; i < elementsSize; i++) {
            assertEquals(values[i], dict.get(keys[i]));
        }

        for (int i = 0; i < elementsSize; i++) {
            dict.remove(keys[i]);
            assertFalse(dict.contains(keys[i]));
            assertEquals(elementsSize - i - 1, dict.size());
        }

        assertNull(dict.put("abc", "cde"));
        assertEquals("cde", dict.get("abc"));
    }

    @Test
    public void testClear() {
        Dictionary dict = instance();
        assertNull(dict.get("abc"));

        final int elementsSize = 500;
        for (int i = 0; i < elementsSize; i++) {
            String key = String.valueOf(i);
            assertNull(dict.put(key, key));
        }

        dict.clear();

        for (int i = 0; i < elementsSize; i++) {
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


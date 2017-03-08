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
    public void testAll() {
        Dictionary dict = instance();
        final String alphabet = "abcdefghijklmnopqrstuvwxyz";
        int sum = 0;
        for (int i = 0; i < alphabet.length(); ++i) {
            for (int j = i; j < alphabet.length(); ++j) {
                assertNull(dict.put(Integer.toString(i * alphabet.length() + j), alphabet.substring(i, j)));
                ++sum;
            }
        }
        for (int i = 0; i < alphabet.length(); ++i) {
            for (int j = i; j < alphabet.length(); ++j) {
                assertEquals(dict.put(Integer.toString(i * alphabet.length() + j), alphabet.substring(i, j)),
                        alphabet.substring(i, j));
            }
        }

        assertEquals(sum, dict.size());
        for (int i = 0; i < alphabet.length(); ++i) {
            for (int j = i; j < alphabet.length(); ++j) {
                assertEquals(alphabet.substring(i, j), dict.get(Integer.toString(i * alphabet.length() + j)));
            }
        }
        dict.clear();
        assertEquals(0, dict.size());
        for (int i = 0; i < alphabet.length(); ++i) {
            for (int j = i; j < alphabet.length(); ++j) {
                assertNull(dict.put(Integer.toString(i * alphabet.length() + j), alphabet.substring(i, j)));
                assertEquals(alphabet.substring(i, j), dict.remove(Integer.toString(i * alphabet.length() + j)));
            }
        }
        assertEquals(0, dict.size());



    }
//    @Test
//    public void testRemove()
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

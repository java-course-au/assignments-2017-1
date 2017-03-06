package ru.spbau.mit;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DictionaryTest {
    private Dictionary dictionary;

    @Before
    public void setUp() throws Exception {
        dictionary = new DictionaryImpl();
    }

    @Test
    public void testPutGet() throws Exception {
        assertNull(dictionary.get("abc"));
        assertNull(dictionary.put("abc", "abc"));
        assertEquals("abc", dictionary.put("abc", "def"));
        assertEquals("def", dictionary.get("abc"));
    }

    @Test
    public void testRemoveContains() throws Exception {
        assertFalse(dictionary.contains("abc"));
        dictionary.put("abc", "def");
        assertTrue(dictionary.contains("abc"));
        assertEquals("def", dictionary.remove("abc"));
        assertFalse(dictionary.contains("abc"));
    }

    @Test
    public void testSizeClear() throws Exception {
        dictionary.put("abc", "123");
        dictionary.put("def", "456");
        dictionary.put("def", "789");
        assertEquals(2, dictionary.size());
        dictionary.remove("def");
        assertEquals(1, dictionary.size());
        dictionary.clear();
        assertEquals(0, dictionary.size());
    }
}

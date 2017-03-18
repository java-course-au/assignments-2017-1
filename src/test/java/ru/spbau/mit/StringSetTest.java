package ru.spbau.mit;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class StringSetTest {

    @Test
    public void testSimple() {
        StringSet stringSet = instance();

        assertTrue(stringSet.add("abc"));
        assertTrue(stringSet.contains("abc"));
        assertEquals(1, stringSet.size());
        assertEquals(1, stringSet.howManyStartsWithPrefix("abc"));

        assertTrue(stringSet.remove("abc"));
        assertFalse(stringSet.contains("abc"));
        assertEquals(0, stringSet.size());
        assertEquals(0, stringSet.howManyStartsWithPrefix("abc"));
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

    @Test
    public void testSimpleSerialization() {
        StringSet stringSet = instance();

        assertTrue(stringSet.add("abc"));
        assertTrue(stringSet.add("cde"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ((StreamSerializable) stringSet).serialize(outputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        StringSet newStringSet = instance();
        ((StreamSerializable) newStringSet).deserialize(inputStream);

        assertTrue(newStringSet.contains("abc"));
        assertTrue(newStringSet.contains("cde"));
    }

    @Test
    public void testEmptySetSerialization() {
        StringSet stringSet = instance();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ((StreamSerializable) stringSet).serialize(outputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        StringSet newStringSet = instance();
        ((StreamSerializable) newStringSet).deserialize(inputStream);

        assertEquals(0, newStringSet.size());
    }

    @Test
    public void testPrefixStringsSerialization() {
        StringSet stringSet = instance();

        assertTrue(stringSet.add("abc"));
        assertTrue(stringSet.add("abcd"));
        assertTrue(stringSet.add("e"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ((StreamSerializable) stringSet).serialize(outputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        StringSet newStringSet = instance();
        ((StreamSerializable) newStringSet).deserialize(inputStream);

        assertTrue(newStringSet.contains("abc"));
        assertTrue(newStringSet.contains("abcd"));
        assertTrue(newStringSet.contains("e"));

        assertEquals(2, newStringSet.howManyStartsWithPrefix("a"));
        assertEquals(2, newStringSet.howManyStartsWithPrefix("abc"));
        assertEquals(1, newStringSet.howManyStartsWithPrefix("abcd"));
        assertEquals(0, newStringSet.howManyStartsWithPrefix("abcde"));
        assertEquals(1, newStringSet.howManyStartsWithPrefix("e"));
    }

    @Test(expected = SerializationException.class)
    public void testSimpleSerializationFails() {
        StringSet stringSet = instance();

        assertTrue(stringSet.add("abc"));
        assertTrue(stringSet.add("cde"));

        OutputStream outputStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException("Fail");
            }
        };

        ((StreamSerializable) stringSet).serialize(outputStream);
    }

    @Test(expected = SerializationException.class)
    public void testSerializationFromEmptyStreamFails() {
        final byte[] buffer = new byte[0];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
        StringSet newStringSet = instance();
        ((StreamSerializable) newStringSet).deserialize(inputStream);
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

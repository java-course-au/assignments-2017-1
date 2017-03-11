package ru.spbau.mit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.Pipe;

public class StringSetTest {
    private StringSetImpl stringSet;
    private StringSetImpl deserializedStringSet;
    private InputStream input;
    private OutputStream output;

    @Before
    public void setUp() throws Exception {
        stringSet = new StringSetImpl();
        deserializedStringSet = new StringSetImpl();
        final Pipe pipe = Pipe.open();
        input = Channels.newInputStream(pipe.source());
        output = Channels.newOutputStream(pipe.sink());
    }

    @After
    public void tearDown() throws Exception {
        input.close();
        output.close();
    }

    @Test
    public void testSerializationSimple() throws IOException {
        stringSet.add("abc");
        stringSet.add("cde");
        stringSet.serialize(output);
        output.close();
        deserializedStringSet.deserialize(input);
        assertEquals(stringSet, deserializedStringSet);
    }

    @Test
    public void testSerializationEmpty() throws IOException {
        stringSet.serialize(output);
        output.close();
        deserializedStringSet.deserialize(input);
        assertEquals(stringSet, deserializedStringSet);
    }

    @Test
    public void testSerializationDeep() throws IOException {
        stringSet.add("");
        stringSet.add("a");
        stringSet.add("abc");
        stringSet.serialize(output);
        output.close();
        deserializedStringSet.deserialize(input);
        assertEquals(stringSet, deserializedStringSet);
    }


    @Test(expected = SerializationException.class)
    public void testSerializationFails() throws IOException {
        output.close();
        deserializedStringSet.deserialize(input);
        fail();
    }
}

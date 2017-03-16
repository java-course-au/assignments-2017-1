import org.junit.Assert;
import org.junit.Test;
import ru.spbau.mit.StreamSerializable;
import ru.spbau.mit.Trie;
import ru.spbau.mit.TrieImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TrieSerializableTest {

    @Test
    public void testSimpleSerialization() {
        TrieImpl trie = new TrieImpl();

        Assert.assertTrue(trie.add("abc"));
        Assert.assertTrue(trie.add("cde"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            trie.serialize(outputStream);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TrieImpl trie1 = new TrieImpl();
        try {
            trie1.deserialize(inputStream);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        Assert.assertTrue(trie1.contains("abc"));
        Assert.assertTrue(trie1.contains("cde"));
    }


    @Test
    public void testSimpleSerializationFails() {
        TrieImpl trie = new TrieImpl();

        Assert.assertTrue(trie.add("abc"));
        Assert.assertTrue(trie.add("cde"));

        OutputStream outputStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException("Fail");
            }
        };

        try {
            trie.serialize(outputStream);
        } catch (IOException ex) {
            Assert.assertEquals("Fail",ex.getMessage());
        }
    }
}

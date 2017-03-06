import org.junit.Assert;
import org.junit.Test;
import ru.spbau.mit.DictionaryImpl;

public class DictionaryTest {

    private DictionaryImpl dictionary = new DictionaryImpl();

    @Test
    public void dictionaryContainsTest() {
        dictionary.put("a", "value a");
        Assert.assertTrue(dictionary.contains("a"));
    }

    @Test
    public void dictionaryRemoveTest() {
        dictionary.put("b", "value b");
        Assert.assertTrue(dictionary.contains("b"));

        dictionary.remove("b");
        Assert.assertFalse(dictionary.contains("b"));
    }

    @Test
    public void dictionaryGetTest() {
        String key = "c";
        String value = "value c";
        dictionary.put(key, value);
        Assert.assertTrue(dictionary.contains(key));

        Assert.assertEquals(value, dictionary.get(key));
    }

    @Test
    public void dictionarySizeTest() {
        DictionaryImpl dictionaryNew = new DictionaryImpl();
        String key[] = new String[]{"a", "b", "c", "d", "e"};
        String value[] = new String[]{"a_value", "b_value", "c_value",
                "d_value", "e_value"};

        assert key.length == value.length;

        for (int i = 0; i < key.length; i++) {
            dictionaryNew.put(key[i], value[i]);
        }
        Assert.assertEquals(key.length, dictionaryNew.size());

        dictionaryNew.remove(key[0]);

        Assert.assertEquals(key.length - 1, dictionaryNew.size());

        for (int i = 1; i < key.length; i++) {
            dictionaryNew.remove(key[i]);
        }
        Assert.assertEquals(0, dictionaryNew.size());
    }


    @Test
    public void bigDictionaryTest() {
        dictionary.clear();
        int size = 1000;
        for (int i = 0; i < size; i++) {
            dictionary.put(Integer.toString(i), Integer.toString(i));
        }
        Assert.assertEquals(size, dictionary.size());
        for (int i = 0; i < size; i++) {
            Assert.assertTrue(dictionary.contains(Integer.toString(i)));
        }

        for (int i = 0; i < size; i++) {
            Assert.assertEquals(Integer.toString(i), dictionary.get(Integer.toString(i)));
        }

        for (int i = 0; i < size; i++) {
            dictionary.remove(Integer.toString(i));
        }

        Assert.assertEquals(0, dictionary.size());

        for (int i = 0; i < size; i++) {
            Assert.assertFalse(dictionary.contains(Integer.toString(i)));
            Assert.assertNull(dictionary.remove(Integer.toString(i)));
        }
    }
}
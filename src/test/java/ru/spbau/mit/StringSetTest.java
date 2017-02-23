package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StringSetTest {

    @Test
    public void testSimple() {
        StringSet stringSet = instance();

        assertTrue(stringSet.add("abc"));
        assertTrue(stringSet.contains("abc"));
        assertEquals(1, stringSet.size());
        assertEquals(1, stringSet.howManyStartsWithPrefix("abc"));
        assertTrue(stringSet.remove("abc"));
        assertTrue(!stringSet.contains("abc"));
    }

    @Test
    public void moreGeneralTest() {
        StringSet stringSet = instance();

        assertTrue(stringSet.add("A"));
        assertTrue(stringSet.add("in"));
        assertTrue(stringSet.add("inn"));
        assertTrue(stringSet.add("to"));
        assertTrue(stringSet.add("tea"));
        assertTrue(stringSet.add("ted"));
        assertTrue(stringSet.add("ten"));

        assertTrue(stringSet.contains("A"));
        assertTrue(stringSet.contains("to"));
        assertTrue(stringSet.contains("tea"));
        assertTrue(stringSet.contains("ted"));
        assertTrue(stringSet.contains("ten"));
        assertTrue(stringSet.contains("inn"));
        assertTrue(stringSet.contains("in"));

        final int size = 7;
        assertTrue(stringSet.size() == size);

        assertTrue(stringSet.howManyStartsWithPrefix("i") == 2);
        assertTrue(stringSet.howManyStartsWithPrefix("in") == 2);
        assertTrue(stringSet.howManyStartsWithPrefix("te")
                == size - (size / 2) - 1); // :)

        assertTrue(stringSet.remove("in"));

        assertTrue(stringSet.size() == size - 1);
        assertTrue(!stringSet.contains("in"));
        assertTrue(stringSet.contains("inn"));
        assertTrue(stringSet.howManyStartsWithPrefix("in") == 1);
        assertTrue(stringSet.howManyStartsWithPrefix("te")
                == size - (size / 2) - 1);
        assertTrue(stringSet.howManyStartsWithPrefix("tea") == 1);
        assertTrue(stringSet.howManyStartsWithPrefix("ted") == 1);
        assertTrue(stringSet.howManyStartsWithPrefix("ten") == 1);

        assertTrue(!stringSet.remove("te"));
        assertTrue(stringSet.size() == size - 1);

        assertTrue(stringSet.howManyStartsWithPrefix("te")
                == size - (size / 2) - 1);
        assertTrue(stringSet.howManyStartsWithPrefix("tea") == 1);
        assertTrue(stringSet.howManyStartsWithPrefix("ted") == 1);
        assertTrue(stringSet.howManyStartsWithPrefix("ten") == 1);

        assertTrue(stringSet.remove("tea"));
        assertTrue(stringSet.size() == size - 2);

        assertTrue(stringSet.howManyStartsWithPrefix("te") == 2);
        assertTrue(stringSet.howManyStartsWithPrefix("tea") == 0);
        assertTrue(stringSet.howManyStartsWithPrefix("ted") == 1);
        assertTrue(stringSet.howManyStartsWithPrefix("ten") == 1);

    }

    @Test
    public void addContainsSizeRemoveSizeContains() {
        StringSet stringSet = instance();

        stringSet.add("abc");
        stringSet.add("abd");
        stringSet.add("bcd");

        assertTrue(stringSet.contains("abc"));
        assertTrue(stringSet.size() == 1 + 1 + 1);
        assertTrue(stringSet.remove("abc"));
        assertTrue(stringSet.size() == 2);
        assertTrue(!stringSet.contains("abc"));
    }

    @Test
    public void addContainsPrefixRemovePrefix() {
        StringSet stringSet = instance();

        stringSet.add("abc");
        stringSet.add("abd");
        stringSet.add("bcd");

        assertTrue(stringSet.contains("abc"));
        assertTrue(stringSet.howManyStartsWithPrefix("a") == 2);
        assertTrue(stringSet.remove("abc"));
        assertTrue(stringSet.howManyStartsWithPrefix("a") == 1);
    }

    @Test
    public void doubleAdd() {
        StringSet stringSet = instance();
        stringSet.add("helloWorldAndVeryLongStringAfter");
        stringSet.add("helloWorldAndVeryLongStringAfter");
        assertTrue(stringSet.size() == 1);
        assertTrue(stringSet.howManyStartsWithPrefix("helloWorld") == 1);
    }

    @Test
    public void doubleDelete() {
        StringSet stringSet = instance();
        stringSet.add("helloWorldAndVeryLongStringAfter");

        assertTrue(stringSet.remove("helloWorldAndVeryLongStringAfter"));
        assertTrue(!stringSet.remove("helloWorldAndVeryLongStringAfter"));

        assertTrue(stringSet.size() == 0);
        assertTrue(stringSet.howManyStartsWithPrefix("helloWorld") == 0);
    }

    @Test
    public void emptyStringsTest() {
        StringSet stringSet = instance();

        stringSet.add("abc");
        assertTrue(stringSet.howManyStartsWithPrefix("") == 1);
        assertTrue(stringSet.add(""));
        assertTrue(stringSet.howManyStartsWithPrefix("") == 2);
        assertTrue(!stringSet.add(""));
        assertTrue(stringSet.howManyStartsWithPrefix("") == 2);
        assertTrue(stringSet.size() == 2);
        assertTrue(stringSet.remove(""));
        assertTrue(stringSet.size() == 1);
        assertTrue(stringSet.contains("abc"));
    }



    public static StringSet instance() {
        try {
            return (StringSet) Class.forName("ru.spbau.mit.StringSetImpl")
                    .newInstance();
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

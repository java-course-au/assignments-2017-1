package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StringSetTest {

    @Test
    public void testSimple() {
        StringSet stringSet = instance();
        assertTrue(stringSet.add("abc"));
        assertTrue(stringSet.add("ab"));
        assertTrue(stringSet.contains("abc"));
        addTest(stringSet);
        containsTest(stringSet);
//        System.out.println(stringSet.howManyStartsWithPrefix(""));
//        assertEquals(2, stringSet.size());
//        assertEquals(1, stringSet.howManyStartsWithPrefix("abc"));
        stringSet.remove("abc");
        stringSet.remove("");
    }

    public static void addTest(StringSet stringSet) {
        String str;
        for (int i = 0; i < 52; i++) {
            if (i >= 26) {
                str = Character.toString((char) (65 + i + 6));
            }
            else {
                str = Character.toString((char) (65 + i));
            }
            assertTrue(stringSet.add(str));
        }
    }

    public static void containsTest(StringSet stringSet) {
        String str;
        for (int i = 0; i < 52; i++) {
            if (i >= 26) {
                str = Character.toString((char) (65 + i + 6));
            }
            else {
                str = Character.toString((char) (65 + i));
            }
            assertTrue(stringSet.contains(str));
        }
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

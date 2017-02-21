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
//        addTest(stringSet);
//        containsTest(stringSet);
        assertEquals(1, stringSet.size());
        assertEquals(1, stringSet.howManyStartsWithPrefix("abc"));
    }

//    public static void addTest(StringSet stringSet) {
//        String str;
//        for (int i = 0; i < StringSetImpl.CHARCOUNT; i++) {
//            if (i >= StringSetImpl.ALPHABET) {
//                str = Character.toString((char) (charA + i + StringSetImpl.CHCNTBWLOWANDUP));
//            }
//            else {
//                str = Character.toString((char) (charA + i));
//            }
//            assertTrue(stringSet.add(str));
//        }
//        assertTrue(stringSet.add("abd"));
//        assertTrue(stringSet.add("abe"));
//        assertTrue(stringSet.add("abf"));
//    }
//
//    public static void containsTest(StringSet stringSet) {
//        String str;
//        for (int i = 0; i < StringSetImpl.CHARCOUNT; i++) {
//            if (i >= StringSetImpl.ALPHABET) {
//                str = Character.toString((char) (charA + i + StringSetImpl.CHCNTBWLOWANDUP));
//            }
//            else {
//                str = Character.toString((char) (charA + i));
//            }
//            assertTrue(stringSet.contains(str));
//        }
//    }

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

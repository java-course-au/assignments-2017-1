package ru.spbau.mit;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static junitx.framework.ComparableAssert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() {
        String prePath = "src/test/resources/";
        List<String> files = Arrays.asList(prePath + "test1.txt", prePath + "test2.txt", prePath + "test3.txt", prePath + "test4.txt");
        List<String> actual = SecondPartTasks.findQuotes(files, "hello");
        List<String> expected = Arrays.asList("hello world", "hello", "hello azaaza");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPiDividedBy4() {
        assertEquals(0.78, Math.floor(SecondPartTasks.piDividedBy4() * 100) / 100d);
    }

    @Test
    public void testFindPrinter() {
        Map<String, List<String>> compositions = new HashMap<>();
        compositions.put("vova", Arrays.asList("first one"));
        compositions.put("vova", Arrays.asList("first one", "second one"));
        compositions.put("oleg", Arrays.asList("first one", "second one", "Reasoning about actions: Non-deterministic effects, Constraints, and Qualification"));

        assertEquals("oleg", SecondPartTasks.findPrinter(compositions));
    }

    @Test
    public void testFindPrinterEmpty() {
        Map<String, List<String>> compositions = new HashMap<>();
        assertTrue(SecondPartTasks.findPrinter(compositions) == null);
    }

    @Test
    public void testCalculateGlobalOrder() {
        List<Map<String, Integer>> goods = new ArrayList<>();
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("cola", 1);
        map1.put("pivo", 2);
        map1.put("moloko", 3);
        goods.add(map1);

        Map<String, Integer> map2 = new HashMap<>();
        map2.put("whiskey", 10);
        map2.put("pechenki", 100);
        map2.put("cakes", 10);
        map2.put("moloko", 10);
        goods.add(map2);

        Map<String, Integer> map3 = new HashMap<>();
        map3.put("pivo", 1);
        goods.add(map3);

        Map<String, Integer> expected = new HashMap<>();
        expected.put("cola", 1);
        expected.put("pivo", 3);
        expected.put("moloko", 13);
        expected.put("whiskey", 10);
        expected.put("pechenki", 100);
        expected.put("cakes", 10);

        Map<String, Integer> actual = SecondPartTasks.calculateGlobalOrder(goods);

        assertTrue(expected.equals(actual));

    }

    @Test
    public void testCalculateGlobalOrderEmpty() {
        List<Map<String, Integer>> goods = new ArrayList<>();
        Map<String, Integer> actual = SecondPartTasks.calculateGlobalOrder(goods);
        Map<String, Integer> expected = new HashMap<>();
        assertTrue(expected.equals(actual));
    }
}

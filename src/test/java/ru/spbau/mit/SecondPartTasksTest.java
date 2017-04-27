package ru.spbau.mit;

import org.junit.Test;

import java.io.File;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() {
        ClassLoader classLoader = getClass().getClassLoader();
        File testFile1 = new File(classLoader.getResource("quotes1").getFile());
        File testFile2 = new File(classLoader.getResource("quotes2").getFile());
        List<String> paths = new ArrayList<>();
        paths.add(testFile1.getAbsolutePath());
        paths.add(testFile2.getAbsolutePath());

        assertEquals(
                Arrays.asList("hello",
                        "hello world",
                        "that is hell down below",
                        "testhelltest",
                        "testheltest testhelltest testhel ltest",
                        "that's new hello world",
                        "ha hello you",
                        "that is a test to find hell string"),
                SecondPartTasks.findQuotes(paths, "hell"));

        assertEquals(
                Arrays.asList("hello",
                        "hello world",
                        "that's new hello world",
                        "ha hello you"),
                SecondPartTasks.findQuotes(paths, "hello"));

        assertEquals(
                new ArrayList<String>(),
                SecondPartTasks.findQuotes(paths, "NO_SUCH_WORD"));
    }

    @Test
    public void testPiDividedBy4() {
        final double probability = SecondPartTasks.piDividedBy4();
        final double precision = 0.01;
        assertTrue(Math.abs(probability - Math.PI / 4) < precision);
    }

    @Test
    public void testFindPrinter() {
        String[] texts = {"abc", "abcd", "abcde"};
        Map<String, List<String>> compositions = new HashMap<>();
        compositions.put("Author1",
                Arrays.asList(texts[1],
                        texts[1],
                        texts[2]));
        compositions.put("Author2",
                Arrays.asList(texts[2],
                        texts[2],
                        texts[2]));
        compositions.put("Author3",
                Arrays.asList(texts[1],
                        texts[0],
                        texts[1]));

        assertEquals("Author2", SecondPartTasks.findPrinter(compositions));
    }

    @Test
    public void testCalculateGlobalOrder() {
        List<Map<String, Integer>> orders = new ArrayList<>();
        orders.add(new HashMap<>());
        orders.add(new HashMap<>());
        orders.add(new HashMap<>());
        orders.get(0).put("Eggs", 1000);
        orders.get(0).put("Milk", 10);
        orders.get(1).put("Eggs", 100);
        orders.get(1).put("Milk", 100);
        orders.get(2).put("Eggs", 10);
        orders.get(2).put("Milk", 1001);

        Map<String, Integer> globalOrder = SecondPartTasks.calculateGlobalOrder(orders);

        assertEquals(new Integer(1110), globalOrder.get("Eggs"));
        assertEquals(new Integer(1111), globalOrder.get("Milk"));
    }
}
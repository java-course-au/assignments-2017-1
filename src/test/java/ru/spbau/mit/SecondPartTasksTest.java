package ru.spbau.mit;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SecondPartTasksTest {

    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    @Test
    public void testFindQuotes() throws IOException {
        File file1 = folder.newFile("file1.txt");
        File file2 = folder.newFile("file2.txt");
        List<String> paths = Arrays.asList(file1.getAbsolutePath(), file2.getAbsolutePath());

        PrintWriter writer = new PrintWriter(file1);
        writer.write("aaaaaaaaa\n");
        writer.write("bbbabbbab\n");
        writer.write("cccbavbbbbaaa\n");
        writer.flush();
        writer.close();

        writer = new PrintWriter(file2);
        writer.write("ddddba\n");
        writer.write("aaab\n");
        writer.flush();
        writer.close();

        List<String> res = Arrays.asList("aaaaaaaaa", "cccbavbbbbaaa", "aaab");
        assertEquals(res, SecondPartTasks.findQuotes(paths, "aaa"));

        res = Arrays.asList("bbbabbbab", "aaab");
        assertEquals(res, SecondPartTasks.findQuotes(paths, "ab"));
    }

    @Test
    public void testPiDividedBy4() {
        final int iterations = 10;
        final double eps = 0.05;
        final double prob = Math.PI / 4;

        for (int i = 0; i < iterations; ++i) {
            assertEquals(prob, SecondPartTasks.piDividedBy4(), eps);
        }
    }

    @Test
    public void testFindPrinter() {
        final List<String> bookSet1 = Arrays.asList("aaa", "bbb", "ccc");
        final List<String> bookSet2 = Arrays.asList("1", "aaaaaaaaaaaaaaaaaaa");
        final List<String> bookSet3 = Arrays.asList("aaa", "bbb", "cc", "", "");

        Map<String, List<String>> sample = new HashMap<>();
        sample.put("a1", bookSet1);
        assertEquals("a1", SecondPartTasks.findPrinter(sample));

        sample.put("a2", bookSet2);
        assertEquals("a2", SecondPartTasks.findPrinter(sample));

        sample.put("a3", bookSet3);
        assertEquals("a2", SecondPartTasks.findPrinter(sample));
    }

    @Test
    public void testCalculateGlobalOrder() {
        final String product1 = "pe1";
        final String product2 = "pr2";
        final String product3 = "pr3";
        final String product4 = "pr4";


        final Map<String, Integer> orders1 = new HashMap<>();
        orders1.put(product1, 3);
        orders1.put(product2, 4);

        final Map<String, Integer> orders2 = new HashMap<>();
        orders2.put(product1, 7);
        orders2.put(product2, 1);
        orders2.put(product3, 8);

        final Map<String, Integer> orders3 = new HashMap<>();
        orders3.put(product4, 13);

        assertEquals(orders1, SecondPartTasks.calculateGlobalOrder(Arrays.asList(orders1)));

        Map<String, Integer> res = new HashMap<>();
        res.put(product1, 10);
        res.put(product2, 5);
        res.put(product3, 8);
        assertEquals(res, SecondPartTasks.calculateGlobalOrder(Arrays.asList(orders1, orders2)));

        res = new HashMap<>();
        res.put(product1, 3);
        res.put(product2, 4);
        res.put(product4, 13);
        assertEquals(res, SecondPartTasks.calculateGlobalOrder(Arrays.asList(orders1, orders3)));
    }
}

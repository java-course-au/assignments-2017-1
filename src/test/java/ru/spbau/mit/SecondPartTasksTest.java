package ru.spbau.mit;

import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static org.junit.Assert.*;

public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() throws IOException {
        TemporaryFolder folder = new TemporaryFolder();
        folder.create();
        File[] files = new File[]{
                folder.newFile("file1.txt"),
                folder.newFile("file2.txt")
        };
        try (PrintWriter pw = new PrintWriter(files[0])) {
            pw.println("abaca");
            pw.println("ddd");
        }
        try (PrintWriter pw = new PrintWriter(files[1])) {
            pw.print("dabaca");
        }
        List<String> quotes = SecondPartTasks.findQuotes(
                Arrays.asList(files[0].getAbsolutePath(), files[1].getAbsolutePath()),
                "bac");

        assertEquals(quotes, Arrays.asList("abaca", "dabaca"));
    }

    @Test
    public void testPiDividedBy4() {
        assertTrue(Math.abs(SecondPartTasks.piDividedBy4() - Math.PI / 4) <= 1e-3);
    }

    @Test
    public void testFindPrinter() {
        Map<String, List<String>> compositions = new HashMap<>();
        compositions.put("a", Arrays.asList("1", "22"));
        compositions.put("b", Arrays.asList("1", "22", "333"));
        assertEquals("b", SecondPartTasks.findPrinter(compositions));
    }

    @Test
    public void testCalculateGlobalOrder() {
        Map<String, Integer> firstOrder = new HashMap<>();
        firstOrder.put("a", 1);
        firstOrder.put("b", 2);

        Map<String, Integer> secondOrder = new HashMap<>();
        secondOrder.put("b", 2);
        secondOrder.put("c", 3);

        Map<String, Integer> compositeOrder = new HashMap<>();
        compositeOrder.put("a", 1);
        compositeOrder.put("b", 4);
        compositeOrder.put("c", 3);

        assertEquals(compositeOrder, SecondPartTasks.calculateGlobalOrder(Arrays.asList(firstOrder, secondOrder)));
    }
}

package ru.spbau.mit;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static ru.spbau.mit.SecondPartTasks.*;
import static org.junit.Assert.*;
import com.google.common.collect.ImmutableMap;


public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() {
        List<String> paths = Arrays.asList(
                "src/test/resources/first.txt",
                "src/test/resources/second.txt",
                "src/test/resources/third.txt",
                "src/test/resources/fourth.txt");

        String quote = "Dark";

        List<String> expected = Arrays.asList(
                "In grim Dark of the future there is only war.",
                "Without the Dark, there can be no Light,");

        List<String> actual = findQuotes(paths, quote);

        assertEquals(expected, actual);
    }

    @Test
    public void testPiDividedBy4() {
        assertEquals(Math.PI / 4.0, piDividedBy4(), 0.001);
    }

    @Test
    public void testFindPrinter() {
        String composer1 = "comp1";
        String composer2 = "comp2";

        List<String> compositions1 = Arrays.asList("I shall know no fear", "Behold your king");
        List<String> compositions2 = Arrays.asList("To infinity and beyond", "blablaland");

        Map<String, List<String>> map = new TreeMap<>();

        map.put(composer1, compositions1);
        map.put(composer2, compositions2);

        assertEquals(composer1, findPrinter(map));
    }

    @Test
    public void testCalculateGlobalOrder() {
        String item1 = "sock";
        String item2 = "candy";
        String item3 = "shroom";
        String item4 = "bra";

        List<Map<String, Integer>> actual = Arrays.asList(
                ImmutableMap.of(item1, 3, item2, 4, item3, 2),
                ImmutableMap.of(item1, 2, item3, 6),
                ImmutableMap.of(item2, 3, item4, 3));

        assertEquals(ImmutableMap.of(item1, 5, item2, 7, item3, 8, item4, 3),
                calculateGlobalOrder(actual));
    }
}
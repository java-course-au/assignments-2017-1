package ru.spbau.mit;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.fail;
import static ru.spbau.mit.SecondPartTasks.findQuotes;
import static ru.spbau.mit.SecondPartTasks.piDividedBy4;

public class SecondPartTasksTest {
    private static Path fileFirst = Paths.get("testFindQuotesFirst");
    private static Path fileSecond = Paths.get("testFindQuotesSecond");
    private static Path fileThird = Paths.get("testFindQuotesThird");

    @BeforeClass
    public static void setUp() throws IOException {
        Files.createFile(fileFirst);
        Files.createFile(fileSecond);
        Files.createFile(fileThird);
    }

    @AfterClass
    public static void tearDown() throws IOException {
        Files.delete(fileFirst);
        Files.delete(fileSecond);
        Files.delete(fileThird);
    }

    @Test
    public void testFindQuotes() throws IOException {
        FileWriter outFirst = new FileWriter(fileFirst.toFile());
        outFirst.write("Hello this mad mad mad\n");
        outFirst.write("World without md!");
        outFirst.close();

        FileWriter outSecond = new FileWriter(fileSecond.toFile());
        outSecond.write("me mate\n");
        outSecond.write("what's up?");
        outSecond.close();

        FileWriter outThird = new FileWriter(fileThird.toFile());
        outThird.write("mad again\n");
        outThird.write("and again\n");
        outThird.write("and mad again");
        outThird.close();

        List<String> resultLines = findQuotes(Arrays.asList(fileFirst.toString(),
                fileSecond.toString(), fileThird.toString()), "mad");
        Assert.assertEquals(Arrays.asList("Hello this mad mad mad",
                "mad again", "and mad again"),
                resultLines);
    }

    @Test
    public void testPiDividedBy4() {
        Assert.assertEquals(Math.PI / 4, piDividedBy4(), 1e-3);
    }

    @Test
    public void testFindPrinter() {
        fail();
    }

    @Test
    public void testCalculateGlobalOrder() {
        fail();
    }
}

package ru.spbau.mit;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.RuleChain;

public class MemLeakTest {
    private static final int LARGE_BUFFER_SIZE = 2 * 1024 * 1024;
    private static Object dump;

    @Rule
    public RuleChain chain;

    private final ExpectedException expectedException = ExpectedException.none();
    private final MemoryLeakLimit leakChecker = new MemoryLeakLimit();

    {
        chain = RuleChain.outerRule(expectedException).around(leakChecker);
    }

    @Test
    public void leakTest() {
        leakChecker.limit(1);
        dump = new Object[LARGE_BUFFER_SIZE];
    }

    @Test
    public void simpleTest() {
        leakChecker.limit(1);
        dump = new Object[1];
    }

    @Test
    public void testMemoryLeakWithException() {
        expectedException.expect(RuntimeException.class);
        leakChecker.limit(1);
        dump = new Object[LARGE_BUFFER_SIZE];
        throw new RuntimeException();
    }
}

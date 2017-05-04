import org.junit.Rule;
import org.junit.Test;
import ru.spbau.mit.MemoryLeakLimit;

public class MemoryLeakLimitTest {

    private static final int BUFFER_SIZE = 1024 * 1024;
    private byte[] buffer;

    @Rule
    public final MemoryLeakLimit leakLimit = new MemoryLeakLimit();

    @Test
    public void testLeak() {
        leakLimit.limit(1);
        buffer = new byte[BUFFER_SIZE];
    }

    @Test
    public void zeroLomitTest() {
        leakLimit.limit(0);
    }
}

import com.github.sharedlibs.stopwatch.SW;
import org.junit.Test;
import org.junit.rules.Stopwatch;

public class SWTest {


    @Test
    public void x() throws InterruptedException {
        SW sw = SW.start();
        sw.split("xxxx");

        Thread.sleep(111);

        sw.split("zzzz");
        sw.split("zzzz");

        sw.printSplits();
    }
}

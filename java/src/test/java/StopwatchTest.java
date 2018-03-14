import com.github.sharedlibs.stopwatch.Stopwatch;
import org.junit.Test;

public class StopwatchTest {


    @Test
    public void x() throws InterruptedException {
        Stopwatch sw = Stopwatch.start();
        sw.split("xxxx");

        Thread.sleep(111);

        sw.split("zzzz");
        sw.split("zzzz");

        sw.printSplits();
    }
}

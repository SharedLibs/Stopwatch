/*
 * Copyright (c) 2018 Shared Libs (https://github.com/sharedlibs)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package test;

import com.github.sharedlibs.stopwatch.Stopwatch;
import com.github.sharedlibs.stopwatch.StopwatchException;
import org.apache.commons.lang3.Range;
import org.junit.Test;

import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.junit.Assert.*;

public class StopwatchTest {

    private static final long WAIT_MIN = 100;
    private static final long WAIT_MAX = 500;
    private static final double TOLERANCE = 1.15;

    @Test
    public void startAndStop() throws InterruptedException {
        Stopwatch sw = Stopwatch.start();

        long wait = sleep();
        long stop = sw.stop();
        assertEquals(stop, sw.elapsed());
        assertWait(wait, stop);

        sleep();
        assertEquals(stop, sw.stop());
        assertEquals(stop, sw.elapsed());
    }

    @Test
    public void split() throws InterruptedException {
        Stopwatch sw = Stopwatch.start();

        assertWait(sleep(), sw.split("l1"));
        assertWait(sleep(), sw.split("l1"));
        assertWait(sleep(), sw.split(null));
        assertWait(sleep(), sw.split(""));

        sw.stop();
        try {
            sw.split("");
            fail();
        } catch (StopwatchException cause) {
        }

        sw.restart();
        assertWait(sleep(), sw.split(""));
    }

    static private long sleep() throws InterruptedException {
        long wait = nextLong(WAIT_MIN, WAIT_MAX);
        Thread.sleep(wait);

        return wait;
    }

    static private void assertWait(long wait, long value) {
        assertTrue(Range.between((double) wait, wait * TOLERANCE).contains((double) value));
    }
}
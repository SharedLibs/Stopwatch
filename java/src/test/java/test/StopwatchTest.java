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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class StopwatchTest {

    private static final long WAIT_MIN = 100;
    private static final long WAIT_MAX = 500;
    private static final double TOLERANCE = 1.15;

    static private long sleep() throws InterruptedException {
        long wait = nextLong(WAIT_MIN, WAIT_MAX);
        Thread.sleep(wait);

        return wait;
    }

    static private void assertTolerantEquals(long expected, long value) {
        assertTrue(Range.between((double) expected, expected * TOLERANCE).contains((double) value));
    }

    @Test
    public void start() throws InterruptedException {
        Stopwatch sw = Stopwatch.start();
        assertTolerantEquals(0, sw.elapsed());

        long wait = sleep();
        assertTolerantEquals(wait, sw.elapsed());

        for (int i = 0; i < 10; i++) {
            wait += sleep();
            assertTolerantEquals(wait, sw.elapsed());
        }
    }

    @Test
    public void pause() throws InterruptedException {
        Stopwatch sw = Stopwatch.start();

        long elapsed = sw.pause().elapsed();
        assertTolerantEquals(0, elapsed);

        for (int i = 0; i < 10; i++) {
            sw.resume();
            elapsed += sleep();
            assertTolerantEquals(elapsed, sw.pause().elapsed());

            sleep();
            assertTolerantEquals(elapsed, sw.elapsed());
        }

        sleep();
        assertTolerantEquals(elapsed, sw.pause().elapsed());
    }

    @Test
    public void split() throws InterruptedException {
        Stopwatch sw = Stopwatch.start();

        assertTolerantEquals(sleep(), sw.split("l1").millis());
        assertTolerantEquals(sleep(), sw.split("l1").millis());
        assertTolerantEquals(sleep(), sw.split(null).millis());
        assertTolerantEquals(sleep(), sw.split("").millis());

        sw.pause();
        try {
            sw.split("");
            fail();
        } catch (StopwatchException cause) {
        }

        sw.resume();
        assertTolerantEquals(sleep(), sw.split("").millis());
    }

    @Test
    public void restart() throws InterruptedException {
        Stopwatch sw = Stopwatch.start();

        sleep();
        sw.pause();
        sleep();
        sw.restart();
        assertTolerantEquals(sleep(), sw.pause().elapsed());

        sleep();
        sw.restart();
        sw.restart();
        assertTolerantEquals(sleep(), sw.split("").millis());
    }

    @Test
    public void clear() throws InterruptedException {
        Stopwatch sw = Stopwatch.start();

        sleep();
        assertEquals(0, sw.pause().clear().elapsed());
    }
}
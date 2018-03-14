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
package com.github.sharedlibs.stopwatch;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class Stopwatch {

    private static ThreadLocal<List<Split>> sharedSplits = new ThreadLocal();

    private static long NULL = -1;

    private List<Split> splits = new ArrayList();

    private long start;

    private long lastSplit = NULL;

    private long stop = NULL;

    private Stopwatch() {
        start = now();
    }

    private static long now() {
        return Instant.now().toEpochMilli();
    }

    public static Stopwatch start() {
        return new Stopwatch();
    }

    private static List<Split> getSharedSplits() {
        if (sharedSplits.get() == null) {
            synchronized (Stopwatch.class) {
                if (sharedSplits.get() == null) {
                    initSharedSplits();
                }
            }
        }

        return sharedSplits.get();
    }

    private static void initSharedSplits() {
        sharedSplits.set(new ArrayList());
    }

    public static void printSharedSplits() {
        printSharedSplits(null);
    }

    public static void printSharedSplits(Consumer<Split> consumer) {
        printSplits(consumer, getSharedSplits());
    }

    private static void printSplits(Consumer<Split> consumer, List<Split> splits) {
        if (consumer != null) {
            splits.forEach(split -> consumer.accept(split));
        } else {
            System.out.print(splits);
        }
    }

    public long elapsed() {
        return (stop == NULL ? now() : stop) - start;
    }

    public long split(String label) throws StopwatchException {
        long now = now();

        if (stop != NULL) {
            throw new StopwatchException("Stopwatch is stopped");
        }

        //split = now();
        //stop = now();

        long elapsed = now - (lastSplit != NULL ? lastSplit : start);
        lastSplit = now;

        addSplit(new Split(label, elapsed));
//        restart();

        return elapsed;
    }

    public long stop() {
        if (stop == NULL) {
            stop = now();
        }

        return stop - start;
    }

    public Stopwatch restart() {
        //start = now();
//        split = NULL;
        stop = NULL;
        return this;
    }

    public Stopwatch clear() {
        if (stop == NULL) {
            throw new StopwatchException("Stopwatch is running");
        }

        getSharedSplits().removeAll(getSplits());
        getSplits().clear();
        return restart();
    }

    private List<Split> getSplits() {
        return splits;
    }

    public void printSplits() {
        printSplits(null);
    }

    public void printSplits(Consumer<Split> consumer) {
        printSplits(consumer, getSplits());
    }

    private void addSplit(Split split) {
        getSplits().add(split);
        getSharedSplits().add(split);
    }
}

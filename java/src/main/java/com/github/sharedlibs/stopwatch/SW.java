package com.github.sharedlibs.stopwatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

public final class SW {

    private static ThreadLocal<List<Split>> sharedSplits = new ThreadLocal();

    private static long NULL = -1;

    private List<Split> splits = new ArrayList();

    private long start;

    private long lastSplit = NULL;

    private long stop = NULL;

    private SW() {
        start = now();
    }

    private static long now() {
        return new Date().getTime();
    }

    public static SW start() {
        return new SW();
    }

    private static List<Split> getSharedSplits() {
        if (sharedSplits.get() == null) {
            synchronized (SW.class) {
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

    public long split(String label) throws StopwatchException {
        if (stop != NULL) {
            throw new StopwatchException("Stopwatch is stopped");
        }

        //split = now();
        //stop = now();
        long now = now();
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

    public SW restart() {
        //start = now();
//        split = NULL;
        stop = NULL;
        return this;
    }

    public SW clear() {
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

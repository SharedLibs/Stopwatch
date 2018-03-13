package org.github.sharedlibs.stopwatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

public final class SW {

    private static ThreadLocal<List<Split>> sharedSplits = new ThreadLocal();

    private static long NULL = -1;

    private List<Split> splits = new ArrayList();

    private long start;

    private long split = NULL;

    private long stop = NULL;

    private SW() {
    }

    private static long now() {
        return new Date().getTime();
    }

    public static SW start() {
        SW stopwatch = new SW();
        return stopwatch.restart();
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

    public static void printShared() {
        print(null, getSharedSplits());
    }

    public static void printShared(Consumer<Split> consumer) {
        print(consumer, getSharedSplits());
    }

    public static void print(Consumer<Split> consumer, List<Split> list) {
        if (consumer != null) {
            for (Split split : list) {
                consumer.accept(split);
            }
        }
    }

    private List<Split> getSplits() {
        return splits;
    }

    public void print() {
        print(null, getSplits());
    }

    public void print(Consumer<Split> consumer) {
        print(consumer, getSplits());
    }

    private void addSplit(Split split) {
        getSplits().add(split);
        getSharedSplits().add(split);
    }

    public SW restart() {
        start = now();
        stop = NULL;
        split = NULL;
        return this;
    }

    public SW clear() {
        getSharedSplits().removeAll(getSplits());
        getSplits().clear();
        return restart();
    }

    public long millis() {
        return (stop == NULL ? now() : stop) - start;
    }

    public long split(String label) {
        stop = now();
        split = now();
        long elapsed = split - start;

        addSplit(new Split(label, elapsed));
        restart();

        return elapsed;
    }

    public class Split {

        final String label;

        final long millis;

        Split(String label, long millis) {
            this.label = label;
            this.millis = millis;
        }

        @Override
        public String toString() {
            return label + ": " + millis;
        }
    }
}

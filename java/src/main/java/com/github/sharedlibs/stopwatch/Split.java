package com.github.sharedlibs.stopwatch;

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

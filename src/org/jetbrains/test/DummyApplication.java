package org.jetbrains.test;

import org.jetbrains.test.tracer.TraceGuard;

import java.util.List;
import java.util.Random;

/**
 * Nikolay.Tropin
 * 18-Apr-17
 */
public class DummyApplication {
    private final List<String> args;
    private Random random = new Random(System.nanoTime());

    public DummyApplication(List<String> args) {
        this.args = args;
    }

    private boolean nextBoolean() {
        return random.nextBoolean();
    }

    private boolean stop() {
        return random.nextDouble() < 0.5;
    }

    private String nextArg() {
        int idx = random.nextInt(args.size());
        return args.get(idx);
    }

    private void sleep() {
        try {
            Thread.sleep(random.nextInt(20));
        } catch (InterruptedException ignored) {

        }
    }

    private void abc(String s) {
        try (TraceGuard ignored = new TraceGuard("abc", s)) {
            sleep();
            if (stop()) {
                //do nothing
            } else if (nextBoolean()) {
                def(nextArg());
            } else {
                xyz(nextArg());
            }
        }
    }

    private void def(String s) {
        try (TraceGuard ignored = new TraceGuard("def", s)) {
            sleep();
            if (stop()) {
                //do nothing
            } else if (nextBoolean()) {
                abc(nextArg());
            } else {
                xyz(nextArg());
            }
        }
    }

    private void xyz(String s) {
        try (TraceGuard ignored = new TraceGuard("xyz", s)) {
            sleep();
            if (stop()) {
                //do nothing
            } else if (nextBoolean()) {
                abc(nextArg());
            } else {
                def(nextArg());
            }
        }
    }

    public void start() {
        abc(nextArg());
    }
}

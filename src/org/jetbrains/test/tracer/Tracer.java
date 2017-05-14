package org.jetbrains.test.tracer;

import java.util.HashMap;
import java.util.Map;

/**
 * Tracer
 */
public final class Tracer {
    private static final Tracer INSTANCE = new Tracer();

    public static Tracer getInstance() {
        return INSTANCE;
    }

    private final Trace trace = new Trace();

    private Tracer() {
    }

    /**
     * Enter given method. Must be paired with traceLeave
     * @param methodName name of entered method
     * @param args arguments of entered method
     */
    public void traceEnter(String methodName, Object... args) {
        trace.traceEnter(methodName, args);
    }

    /**
     * Exit last entered method.
     */
    public void traceLeave() {
        trace.traceLeave();
    }

    public Trace getTrace() {
        return trace;
    }
}

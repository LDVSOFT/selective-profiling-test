package org.jetbrains.test.tracer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Global trace.
 */
public class Trace {
    private final Map<String, ThreadTrace> threadTraces;

    Trace(Map<String, ThreadTrace> threadTraces) {
        this.threadTraces = threadTraces;
    }

    Trace() {
        this(new HashMap<>());
    }

    public Map<String, ThreadTrace> getThreadTraces() {
        return Collections.unmodifiableMap(threadTraces);
    }

    private ThreadTrace getThreadTrace() {
        String name = Thread.currentThread().getName();
        if (!threadTraces.containsKey(name))
             threadTraces.put(name, new ThreadTrace());
        return threadTraces.get(name);
    }

    void traceEnter(String methodName, Object... args) {
        getThreadTrace().traceEnter(methodName, args);
    }

    void traceLeave() {
        getThreadTrace().traceLeave();
    }
}

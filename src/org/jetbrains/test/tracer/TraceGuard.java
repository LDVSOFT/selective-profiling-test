package org.jetbrains.test.tracer;

/**
 * Created by ldvsoft on 14.05.17.
 */
public class TraceGuard implements AutoCloseable {
    public TraceGuard(String methodName, Object... args) {
        Tracer.getInstance().traceEnter(methodName, args);
    }

    @Override
    public void close() {
        Tracer.getInstance().traceLeave();
    }
}

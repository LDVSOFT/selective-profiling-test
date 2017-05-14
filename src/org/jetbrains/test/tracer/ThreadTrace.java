package org.jetbrains.test.tracer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tracer of a single thread.
 */
class ThreadTrace {
    private final List<MethodTrace> roots;
    private MethodTrace currentMethod = null;

    ThreadTrace(List<MethodTrace> roots) {
        this.roots = roots;
    }

    ThreadTrace() {
        this(new ArrayList<>());
    }

    void traceEnter(String methodName, Object... args) {
        List<String> argsAsStrings = Arrays.stream(args)
                .map(Object::toString)
                .collect(Collectors.toList());
        MethodTrace newCallTrace = new MethodTrace(currentMethod, methodName, argsAsStrings);
        if (currentMethod == null) {
            roots.add(newCallTrace);
        }
        currentMethod = newCallTrace;
    }

    void traceLeave() {
        currentMethod = currentMethod.getParent();
    }

    public List<MethodTrace> getRoots() {
        return Collections.unmodifiableList(roots);
    }
}

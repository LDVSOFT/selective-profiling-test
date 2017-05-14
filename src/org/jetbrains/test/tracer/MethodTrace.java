package org.jetbrains.test.tracer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Trace of a single method call, containing all traced subcalls.
 */
public final class MethodTrace {
    private final MethodTrace parent;
    private final String methodName;
    private final List<String> arguments;
    private final List<MethodTrace> children;

    MethodTrace(MethodTrace parent, String methodName, List<String> arguments, List<MethodTrace> children) {
        if (parent != null)
            parent.addChild(this);
        this.parent = parent;
        this.methodName = methodName;
        this.arguments = Collections.unmodifiableList(arguments);
        this.children = children;
    }

    MethodTrace(MethodTrace parent, String methodName, List<String> arguments) {
        this(parent, methodName, arguments, new ArrayList<>());
    }

    MethodTrace(String methodName, List<String> arguments) {
        this(null, methodName, arguments);
    }

    public MethodTrace getParent() {
        return parent;
    }

    public String getName() {
        return methodName;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public List<MethodTrace> getChildren() {
        return Collections.unmodifiableList(children);
    }

    void addChild(MethodTrace trace) {
        children.add(trace);
    }
}

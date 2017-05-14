package org.jetbrains.test.tracer;

/**
 * Pretty printer of a tracer
 */
public class PrettyPrinter {
    private static final String OFFSET = ". . ";

    private int level = 1;
    private final Trace trace;

    /**
     * Create pretty printer
     * @param trace trace to print
     */
    public PrettyPrinter(Trace trace) {
        this.trace = trace;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        trace.getThreadTraces().forEach((threadName, threadTrace) -> {
            buffer.append(String.format("Thread %s:\n", threadName));
            for (MethodTrace trace: threadTrace.getRoots()) {
                printTrace(buffer, trace);
            }
        });
        return buffer.toString();
    }

    private void printTrace(StringBuffer buffer, MethodTrace trace) {
        for (int i = 0; i < level; ++i)
            buffer.append(OFFSET);
        buffer.append(String.format(
                "%s(%s)\n",
                trace.getName(),
                String.join(", ", trace.getArguments())
        ));
        ++level;
        for (MethodTrace child: trace.getChildren()) {
            printTrace(buffer, child);
        }
        --level;
    }
}

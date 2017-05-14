package org.jetbrains.test.tracer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Saves and loads trace
 */
public final class FileStorage {
    private FileStorage() {
    }

    public static void saveTo(Trace trace, Path path) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(Files.newOutputStream(path))) {
            Map<String, ThreadTrace> threadTraces = trace.getThreadTraces();
            dos.writeInt(threadTraces.size());
            for (Map.Entry<String, ThreadTrace> entry: threadTraces.entrySet()) {
                String threadName = entry.getKey();
                ThreadTrace threadTrace = entry.getValue();
                List<MethodTrace> roots = threadTrace.getRoots();

                dos.writeUTF(threadName);
                dos.writeInt(roots.size());
                for (MethodTrace root: roots)
                    saveMethodTrace(dos, root);
            }
            dos.flush();
        }
    }

    private static void saveMethodTrace(DataOutputStream dos, MethodTrace trace) throws IOException {
        dos.writeUTF(trace.getName());
        List<String> args = trace.getArguments();
        dos.writeInt(args.size());
        for (String arg: args)
            dos.writeUTF(arg);
        List<MethodTrace> children = trace.getChildren();
        dos.writeInt(children.size());
        for (MethodTrace child: children)
            saveMethodTrace(dos, child);
    }

    public static Trace loadFrom(Path path) throws IOException {
        try (DataInputStream dis = new DataInputStream(Files.newInputStream(path))) {
            Map<String, ThreadTrace> threadTraces = new HashMap<>();
            int threads = dis.readInt();
            for (int i = 0; i < threads; i++) {
                String threadName = dis.readUTF();
                int rootsSize = dis.readInt();
                List<MethodTrace> roots = new ArrayList<>(rootsSize);
                for (int j = 0; j < rootsSize; j++)
                    roots.add(readMethodTrace(dis, null));
                threadTraces.put(threadName, new ThreadTrace(roots));
            }
            return new Trace(threadTraces);
        }
    }

    private static MethodTrace readMethodTrace(DataInputStream dis, MethodTrace parent) throws IOException {
        String name = dis.readUTF();
        int argsSize = dis.readInt();
        List<String> args = new ArrayList<>(argsSize);
        for (int i = 0; i < argsSize; i++)
            args.add(dis.readUTF());
        int childrenSize = dis.readInt();
        List<MethodTrace> children = new ArrayList<>(childrenSize);
        MethodTrace trace = new MethodTrace(parent, name, args, children);
        for (int i = 0; i < childrenSize; i++)
            readMethodTrace(dis, trace);
        return trace;
    }
}

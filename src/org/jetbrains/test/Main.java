package org.jetbrains.test;

import org.jetbrains.test.tracer.FileStorage;
import org.jetbrains.test.tracer.PrettyPrinter;
import org.jetbrains.test.tracer.Trace;
import org.jetbrains.test.tracer.Tracer;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        ExecutorService service = Executors.newFixedThreadPool(3);
        for(int i = 0; i < 5; i++) {
            int start = 100 * i;
            List<String> arguments = IntStream.range(start, start + 10)
                    .mapToObj(Integer :: toString)
                    .collect(Collectors.toList());
            service.submit(() -> new DummyApplication(arguments).start());
        }
        service.shutdown();
        service.awaitTermination(1, TimeUnit.MINUTES);
        System.out.print(new PrettyPrinter(Tracer.getInstance().getTrace()));
        FileStorage.saveTo(Tracer.getInstance().getTrace(), Paths.get("trace.bin"));
        Trace trace = FileStorage.loadFrom(Paths.get("trace.bin"));
        System.out.print(new PrettyPrinter(trace));
    }
}

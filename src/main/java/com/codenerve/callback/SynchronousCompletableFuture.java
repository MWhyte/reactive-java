package com.codenerve.callback;

import org.apache.log4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SynchronousCompletableFuture {

    private final static Logger logger = Logger.getLogger(SynchronousCompletableFuture.class);

    public static void main(String[] args) {

        logger.info("running app");

        CompletableFuture
                .supplyAsync(action())
                .thenApply(callback())
                .thenAccept(callback2());

        logger.info("calling thread finished");
    }

    private static Supplier<String> action() {
        logger.info("performing some action synchronously");
        return () -> "Something";
    }

    private static Function<String, String> callback() {
        logger.info("callback invoked");
        return str -> str;
    }

    private static Consumer<String> callback2() {
        logger.info("callback2 invoked");
        return logger::info;
    }
}
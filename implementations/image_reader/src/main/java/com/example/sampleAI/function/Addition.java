package com.example.sampleAI.function;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

@Slf4j
public class Addition implements Function<Addition.Request, Addition.Response> {
    public record Request(int a, int b) {}
    public record Response(int c) {}

    public com.example.sampleAI.function.Addition.Response apply(com.example.sampleAI.function.Addition.Request request) {
        log.info("Function Apply invoked:: {} :: {}","Addition", request);
        return new com.example.sampleAI.function.Addition.Response(request.a + request.b);
    }
}

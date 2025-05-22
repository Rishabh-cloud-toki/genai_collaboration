package com.example.sampleAI.function;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

@Slf4j
public class Multiplication implements Function<Multiplication.Request, Multiplication.Response> {

    public record Request(int a, int b) {}
    public record Response(int c) {}

    public Response apply(Request request) {
        log.info("Function Apply invoked:: {} :: {}","Multiplication", request);
        return new Response(request.a* request.b);
    }
}

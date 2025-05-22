package com.example.sampleAI.config;

import com.example.sampleAI.function.Addition;
import com.example.sampleAI.function.Multiplication;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FunctionCallbackConfig {

    @Bean
    public FunctionCallback multiplicationInfo() {

        return FunctionCallback.builder()
                .function("Multiplication", new Multiplication()) // (1) function name and instance
                .description("Performs multiplication of two numbers.") // (2) function description
                .inputType(Multiplication.Request.class) // (3) function input type
                .build();
    }

    @Bean
    public FunctionCallback additionInfo() {

        return FunctionCallback.builder()
                .function("Addition", new Addition()) // (1) function name and instance
                .description("Performs addition of two numbers.") // (2) function description
                .inputType(Addition.Request.class) // (3) function input type
                .build();
    }
}

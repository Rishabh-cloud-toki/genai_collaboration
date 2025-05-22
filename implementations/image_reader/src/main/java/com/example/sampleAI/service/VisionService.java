package com.example.sampleAI.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Map;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class VisionService {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public String analyzeImage(File file) throws IOException {
        log.info("Received request to analyze image");
        byte[] data = Files.readAllBytes(file.toPath());
        String base64 = Base64.getEncoder().encodeToString(data);

        Map<String, Object> body = generateMap(base64);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(
            "https://api.openai.com/v1/chat/completions",
            HttpMethod.POST,
            requestEntity,
            JsonNode.class
        );

        log.info("Response from chat completion :: {}", response);
        JsonNode root = response.getBody();
        assert root != null;
        return root.get("choices").get(0).get("message").get("content").asText();
    }

    private static Map<String, Object> generateMap(String base64) {
        Map<String, Object> textMsg = Map.of(
            "type", "text",
            "text", "What is in this image? If you cant specifically find what is in image, you can tell at a " +
                        "generic level like its a human, animal, some object etc. " +
                        "If you can identify, try to provide more details of this image."
        );
        Map<String, Object> imageMsg = Map.of(
            "type", "image_url",
            "image_url", Map.of("url", "data:image/jpeg;base64," + base64)
        );
        Map<String, Object> userMsg = Map.of(
            "role", "user",
            "content", List.of(textMsg, imageMsg)
        );
        return Map.of(
            "model", "gpt-4o",
            "messages", List.of(userMsg)
        );
    }
}
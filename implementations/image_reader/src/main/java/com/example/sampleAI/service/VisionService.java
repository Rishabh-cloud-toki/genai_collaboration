package com.example.sampleAI.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
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
public class VisionService {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public VisionService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * Reads the given image file, sends it to the GPT-4 Vision preview model,
     * and returns the model's textual description of the image.
     * @param file the image file to analyze
     * @return the model's description of the image
     * @throws IOException if an I/O error occurs reading the file
     */
    public String analyzeImage(File file) throws IOException {
        byte[] data = Files.readAllBytes(file.toPath());
        String base64 = Base64.getEncoder().encodeToString(data);

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
        Map<String, Object> body = Map.of(
            "model", "gpt-4o",
            "messages", List.of(userMsg)
        );

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
        JsonNode root = response.getBody();
        return root.get("choices").get(0).get("message").get("content").asText();
    }
}
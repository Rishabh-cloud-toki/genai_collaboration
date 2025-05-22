package com.example.sampleAI.controller;

import com.example.sampleAI.entity.ConversationRequest;
import com.example.sampleAI.entity.ConversationResponse;
import com.example.sampleAI.service.AiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import com.example.sampleAI.service.VisionService;

@RestController
@Slf4j
public class MainController {

    @Autowired
    AiService service;
    
    @Autowired
    VisionService visionService;

    @PostMapping("/converse")
    public ResponseEntity<ConversationResponse> sendResponse(@RequestBody  ConversationRequest request) {
        log.info("Request :: {}", request);
        ConversationResponse response = service.getResponse(request);

        log.info("Response :: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/start")
    public ResponseEntity<String> start() {
        String conversationId = service.start();
        log.info("Response Conversation Id :: {}", conversationId);
        return ResponseEntity.ok(conversationId);
    }

    /**
     * Endpoint to analyze an uploaded image and generate a description using the AI service.
     * @param file the uploaded image file
     * @return generated description
     * @throws IOException if an I/O error occurs processing the file
     */
    @PostMapping("/from-image")
    public ResponseEntity<String> fromImage(@RequestParam("file") MultipartFile file) throws IOException {
        File imageFile = File.createTempFile("img", ".tmp");
        file.transferTo(imageFile);
        // Call GPT-4 Vision preview to get a description directly
        String description = visionService.analyzeImage(imageFile);
        imageFile.delete();
        return ResponseEntity.ok(description);
    }
}

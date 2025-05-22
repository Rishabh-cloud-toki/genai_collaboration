package com.example.sampleAI.service;

import com.example.sampleAI.entity.ConversationRequest;
import com.example.sampleAI.entity.ConversationResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.example.sampleAI.prompt.Prompt.AGENT_INITIATION;
import static com.example.sampleAI.type.Role.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiService {

    private final OpenAiChatModel chatModel;
    private final SessionManager sessionManager;
    private OpenAiChatOptions options;
    private final List<FunctionCallback> functionCallbacks;

    @PostConstruct
    void configure() {
        options = OpenAiChatOptions.builder()
                .functionCallbacks(functionCallbacks)
                .build();
    }

    public ConversationResponse getResponse(ConversationRequest request) {
        sessionManager.addToMemory(request.getConversationId(), request.getPrompt(), USER);
        ChatResponse response = call(request);
        String responseStr = response.getResult().getOutput().getText();
        sessionManager.addToMemory(request.getConversationId(), responseStr, ASSISTANT);
        return ConversationResponse.builder().output(responseStr).build();
    }

    public String start() {
        String conversationId = getConversationId();
        sessionManager.addToMemory(conversationId, AGENT_INITIATION, USER);
        ChatResponse response = call(ConversationRequest.builder()
                .conversationId(conversationId)
                .prompt(AGENT_INITIATION)
                .build());
        String responseStr = response.getResult().getOutput().getText();
        sessionManager.addToMemory(conversationId, responseStr, ASSISTANT);
        return conversationId;
    }

    public String getConversationId() {
        return UUID.randomUUID().toString();
    }

    private ChatResponse call(ConversationRequest request) {
        List<Message> conversationHistory = sessionManager.getMessages(request.getConversationId());
        Prompt prompt = new Prompt(conversationHistory, options);
        ChatResponse response = chatModel.call(prompt);
        log.info("Output from LLM {}", response);
        return response;
    }
}

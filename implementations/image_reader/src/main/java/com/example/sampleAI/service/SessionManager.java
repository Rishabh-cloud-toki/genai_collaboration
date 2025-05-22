package com.example.sampleAI.service;

import com.example.sampleAI.type.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.sampleAI.type.Role.*;

@Service
@RequiredArgsConstructor
public class SessionManager {

    private final ChatMemory chatMemory;

    public void addToMemory(String conversationId, String input, Role role) {
        if(role == USER) {
            chatMemory.add(conversationId, new UserMessage(input));
        } else {
            chatMemory.add(conversationId, new AssistantMessage(input));
        }
    }

    public List<Message> getMessages(String conversationId) {
        return chatMemory.get(conversationId, 10);
    }

}

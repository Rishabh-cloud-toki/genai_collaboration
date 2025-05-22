package com.example.sampleAI.entity;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
@Builder
public class ConversationRequest {
    private String conversationId;
    private String prompt;
}

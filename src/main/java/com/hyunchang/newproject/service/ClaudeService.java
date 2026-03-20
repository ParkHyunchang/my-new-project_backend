package com.hyunchang.newproject.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hyunchang.newproject.dto.ChatMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class ClaudeService {

    private final RestClient restClient;
    private final String apiKey;

    public ClaudeService(@Value("${anthropic.api-key}") String apiKey) {
        this.apiKey = apiKey;
        this.restClient = RestClient.builder()
                .baseUrl("https://api.anthropic.com")
                .build();
    }

    public String chat(List<ChatMessage> messages) {
        List<AnthropicMessage> anthropicMessages = messages.stream()
                .map(m -> new AnthropicMessage(m.getRole(), m.getContent()))
                .toList();

        AnthropicRequest request = new AnthropicRequest("claude-sonnet-4-6", 1024, anthropicMessages);

        AnthropicResponse response = restClient.post()
                .uri("/v1/messages")
                .header("x-api-key", apiKey)
                .header("anthropic-version", "2023-06-01")
                .header("Content-Type", "application/json")
                .body(request)
                .retrieve()
                .body(AnthropicResponse.class);

        if (response != null && response.content() != null && !response.content().isEmpty()) {
            return response.content().get(0).text();
        }
        return "";
    }

    record AnthropicMessage(String role, String content) {}

    record AnthropicRequest(
            String model,
            @JsonProperty("max_tokens") int maxTokens,
            List<AnthropicMessage> messages
    ) {}

    record AnthropicResponse(List<ContentBlock> content) {}

    record ContentBlock(String type, String text) {}
}

package com.hyunchang.newproject.controller;

import com.hyunchang.newproject.dto.ChatMessage;
import com.hyunchang.newproject.dto.ChatRequest;
import com.hyunchang.newproject.dto.ChatResponse;
import com.hyunchang.newproject.entity.ChatRecord;
import com.hyunchang.newproject.entity.ChatSession;
import com.hyunchang.newproject.service.ChatHistoryService;
import com.hyunchang.newproject.service.ClaudeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ClaudeService claudeService;
    private final ChatHistoryService chatHistoryService;

    public ChatController(ClaudeService claudeService, ChatHistoryService chatHistoryService) {
        this.claudeService = claudeService;
        this.chatHistoryService = chatHistoryService;
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        ChatSession session = chatHistoryService.getOrCreateSession(
                request.getSessionKey(), request.getUsername());

        List<ChatRecord> history = chatHistoryService.getMessages(session);

        List<ChatMessage> messages = new ArrayList<>();
        history.forEach(r -> messages.add(new ChatMessage(r.getRole(), r.getContent())));
        messages.add(new ChatMessage("user", request.getContent()));

        String response = claudeService.chat(messages);

        chatHistoryService.saveMessage(session, "user", request.getContent());
        chatHistoryService.saveMessage(session, "assistant", response);

        return ResponseEntity.ok(new ChatResponse(response));
    }

    @GetMapping("/chat/history/{sessionKey}")
    public List<ChatHistoryService.MessageDto> getHistory(@PathVariable String sessionKey) {
        return chatHistoryService.getHistory(sessionKey);
    }

    @GetMapping("/chat/sessions/{username}")
    public List<ChatHistoryService.SessionSummaryDto> getSessions(@PathVariable String username) {
        return chatHistoryService.getSessionsByUsername(username);
    }

    @PostMapping("/chat/sessions/by-keys")
    public List<ChatHistoryService.SessionSummaryDto> getSessionsByKeys(@RequestBody List<String> keys) {
        return chatHistoryService.getSessionsByKeys(keys);
    }
}

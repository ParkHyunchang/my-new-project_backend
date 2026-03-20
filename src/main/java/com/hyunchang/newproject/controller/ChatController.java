package com.hyunchang.newproject.controller;

import com.hyunchang.newproject.dto.ChatMessage;
import com.hyunchang.newproject.dto.ChatRequest;
import com.hyunchang.newproject.dto.ChatResponse;
import com.hyunchang.newproject.entity.ChatRecord;
import com.hyunchang.newproject.entity.ChatSession;
import com.hyunchang.newproject.exception.ClaudeApiException;
import com.hyunchang.newproject.service.ChatHistoryService;
import com.hyunchang.newproject.service.ClaudeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
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
        String sessionKey = (request.getSessionKey() != null && !request.getSessionKey().isBlank())
                ? request.getSessionKey()
                : UUID.randomUUID().toString();

        String preview = request.getContent() != null && request.getContent().length() > 50
                ? request.getContent().substring(0, 50) + "..."
                : request.getContent();
        log.info("[채팅] user={}, session={}, message={}", request.getUsername(), sessionKey, preview);

        ChatSession session = chatHistoryService.getOrCreateSession(sessionKey, request.getUsername(), request.getAnonId());

        List<ChatRecord> history = chatHistoryService.getMessages(session);

        List<ChatMessage> messages = new ArrayList<>();
        history.forEach(r -> messages.add(new ChatMessage(r.getRole(), r.getContent())));
        messages.add(new ChatMessage("user", request.getContent()));

        try {
            String response = claudeService.chat(messages);
            chatHistoryService.saveMessage(session, "user", request.getContent());
            chatHistoryService.saveMessage(session, "assistant", response);
            return ResponseEntity.ok(new ChatResponse(response));
        } catch (ClaudeApiException e) {
            // 유저 메시지는 저장하되, 에러 메시지는 DB에 저장하지 않음
            chatHistoryService.saveMessage(session, "user", request.getContent());
            return ResponseEntity.ok(new ChatResponse(e.getUserMessage()));
        }
    }

    @GetMapping("/chat/history/{sessionKey}")
    public List<ChatHistoryService.MessageDto> getHistory(@PathVariable String sessionKey) {
        return chatHistoryService.getHistory(sessionKey);
    }

    @GetMapping("/chat/sessions/{username}")
    public List<ChatHistoryService.SessionSummaryDto> getSessions(@PathVariable String username) {
        log.info("[채팅세션 조회] user={}", username);
        return chatHistoryService.getSessionsByUsername(username);
    }

    @PostMapping("/chat/sessions/by-keys")
    public List<ChatHistoryService.SessionSummaryDto> getSessionsByKeys(@RequestBody List<String> keys) {
        return chatHistoryService.getSessionsByKeys(keys);
    }

    @GetMapping("/chat/sessions/anon/{anonId}")
    public List<ChatHistoryService.SessionSummaryDto> getAnonSessions(@PathVariable String anonId) {
        return chatHistoryService.getSessionsByAnonId(anonId);
    }
}

package com.hyunchang.newproject.controller;

import com.hyunchang.newproject.service.ChatHistoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/chat")
public class AdminChatController {

    private final ChatHistoryService chatHistoryService;

    public AdminChatController(ChatHistoryService chatHistoryService) {
        this.chatHistoryService = chatHistoryService;
    }

    @GetMapping("/sessions")
    public List<ChatHistoryService.SessionSummaryDto> sessions() {
        return chatHistoryService.getAllSessionSummaries();
    }

    @GetMapping("/sessions/{sessionKey}/messages")
    public List<ChatHistoryService.MessageDto> messages(@PathVariable String sessionKey) {
        return chatHistoryService.getHistory(sessionKey);
    }
}

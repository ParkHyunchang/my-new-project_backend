package com.hyunchang.newproject.repository;

import com.hyunchang.newproject.entity.ChatRecord;
import com.hyunchang.newproject.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRecordRepository extends JpaRepository<ChatRecord, Long> {
    List<ChatRecord> findByChatSessionOrderByCreatedAtAsc(ChatSession chatSession);
    long countByChatSession(ChatSession chatSession);
}

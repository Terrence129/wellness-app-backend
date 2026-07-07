package com.wellness.wellnessappbackend.ai.chat;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

public interface AiChatMessageRepository extends JpaRepository<AiChatMessage, Long> {

    List<AiChatMessage> findByUserIdAndConversationIdOrderByCreatedAtDesc(
            Long userId,
            String conversationId,
            Pageable pageable
    );
}

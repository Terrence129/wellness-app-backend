package com.wellness.wellnessappbackend.ai.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

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

    @Query(
            value = """
                    select message.conversationId from AiChatMessage message
                    where message.user.id = :userId
                    group by message.conversationId
                    order by max(message.createdAt) desc
                    """,
            countQuery = """
                    select count(distinct message.conversationId) from AiChatMessage message
                    where message.user.id = :userId
                    """
    )
    Page<String> findConversationIdsByUserIdOrderByLastMessageAtDesc(@Param("userId") Long userId, Pageable pageable);

    Page<AiChatMessage> findByUserIdAndConversationId(Long userId, String conversationId, Pageable pageable);

    Optional<AiChatMessage> findFirstByUserIdAndConversationIdOrderByCreatedAtAsc(Long userId, String conversationId);

    Optional<AiChatMessage> findFirstByUserIdAndConversationIdOrderByCreatedAtDesc(Long userId, String conversationId);

    long countByUserIdAndConversationId(Long userId, String conversationId);

    boolean existsByUserIdAndConversationId(Long userId, String conversationId);
}

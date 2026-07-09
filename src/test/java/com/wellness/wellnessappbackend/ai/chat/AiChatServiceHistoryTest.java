package com.wellness.wellnessappbackend.ai.chat;

import com.wellness.wellnessappbackend.ai.AiClient;
import com.wellness.wellnessappbackend.ai.chat.dto.AiChatConversationDto;
import com.wellness.wellnessappbackend.exception.ApiException;
import com.wellness.wellnessappbackend.exception.ErrorCode;
import com.wellness.wellnessappbackend.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AiChatServiceHistoryTest {

    @Mock
    private AiClient aiClient;

    @Mock
    private AiChatMapper aiChatMapper;

    @Mock
    private AiChatMessageRepository aiChatMessageRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AiChatService aiChatService;

    @Test
    void listConversationsKeepsLatestActivityOrderAndBuildsSummaries() {
        String olderConversationId = UUID.randomUUID().toString();
        String newerConversationId = UUID.randomUUID().toString();
        PageRequest pageable = PageRequest.of(0, 20);
        when(aiChatMessageRepository.findConversationIdsByUserIdOrderByLastMessageAtDesc(7L, pageable))
                .thenReturn(new PageImpl<>(List.of(newerConversationId, olderConversationId), pageable, 2));

        AiChatMessage newerFirst = message(newerConversationId, AiChatRole.USER, "hello", Instant.parse("2026-07-09T01:00:00Z"));
        AiChatMessage newerLast = message(newerConversationId, AiChatRole.ASSISTANT, "new answer", Instant.parse("2026-07-09T01:02:00Z"));
        AiChatMessage olderFirst = message(olderConversationId, AiChatRole.USER, "older", Instant.parse("2026-07-08T01:00:00Z"));
        AiChatMessage olderLast = message(
                olderConversationId,
                AiChatRole.ASSISTANT,
                "a".repeat(170),
                Instant.parse("2026-07-08T01:02:00Z")
        );

        when(aiChatMessageRepository.findFirstByUserIdAndConversationIdOrderByCreatedAtAsc(7L, newerConversationId))
                .thenReturn(Optional.of(newerFirst));
        when(aiChatMessageRepository.findFirstByUserIdAndConversationIdOrderByCreatedAtDesc(7L, newerConversationId))
                .thenReturn(Optional.of(newerLast));
        when(aiChatMessageRepository.countByUserIdAndConversationId(7L, newerConversationId)).thenReturn(2L);
        when(aiChatMessageRepository.findFirstByUserIdAndConversationIdOrderByCreatedAtAsc(7L, olderConversationId))
                .thenReturn(Optional.of(olderFirst));
        when(aiChatMessageRepository.findFirstByUserIdAndConversationIdOrderByCreatedAtDesc(7L, olderConversationId))
                .thenReturn(Optional.of(olderLast));
        when(aiChatMessageRepository.countByUserIdAndConversationId(7L, olderConversationId)).thenReturn(4L);

        Page<AiChatConversationDto> result = aiChatService.listConversations(7L, pageable);

        assertThat(result.getContent()).extracting(AiChatConversationDto::conversationId)
                .containsExactly(newerConversationId, olderConversationId);
        assertThat(result.getContent().get(0).messageCount()).isEqualTo(2L);
        assertThat(result.getContent().get(0).lastRole()).isEqualTo("ASSISTANT");
        assertThat(result.getContent().get(0).lastMessagePreview()).isEqualTo("new answer");
        assertThat(result.getContent().get(1).lastMessagePreview()).hasSize(163).endsWith("...");
    }

    @Test
    void getMessagesUsesCurrentUserAndConversationId() {
        String conversationId = UUID.randomUUID().toString();
        PageRequest pageable = PageRequest.of(0, 50, Sort.by("createdAt").ascending());
        AiChatMessage first = message(conversationId, AiChatRole.USER, "hello", Instant.parse("2026-07-09T01:00:00Z"));
        AiChatMessage second = message(conversationId, AiChatRole.ASSISTANT, "answer", Instant.parse("2026-07-09T01:01:00Z"));
        Page<AiChatMessage> page = new PageImpl<>(List.of(first, second), pageable, 2);
        when(aiChatMessageRepository.existsByUserIdAndConversationId(7L, conversationId)).thenReturn(true);
        when(aiChatMessageRepository.findByUserIdAndConversationId(7L, conversationId, pageable)).thenReturn(page);

        Page<AiChatMessage> result = aiChatService.getMessages(7L, conversationId, pageable);

        assertThat(result.getContent()).containsExactly(first, second);
        verify(aiChatMessageRepository).findByUserIdAndConversationId(7L, conversationId, pageable);
    }

    @Test
    void getMessagesRejectsInvalidConversationId() {
        PageRequest pageable = PageRequest.of(0, 50);

        assertThatThrownBy(() -> aiChatService.getMessages(7L, "not-a-uuid", pageable))
                .isInstanceOfSatisfying(ApiException.class, ex -> {
                    assertThat(ex.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
                    assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.VALIDATION_ERROR);
                    assertThat(ex.getMessage()).isEqualTo("conversationId must be a valid UUID");
                });
    }

    @Test
    void getMessagesReturnsNotFoundForMissingOrOtherUserConversation() {
        String conversationId = UUID.randomUUID().toString();
        PageRequest pageable = PageRequest.of(0, 50);
        when(aiChatMessageRepository.existsByUserIdAndConversationId(7L, conversationId)).thenReturn(false);

        assertThatThrownBy(() -> aiChatService.getMessages(7L, conversationId, pageable))
                .isInstanceOfSatisfying(ApiException.class, ex -> {
                    assertThat(ex.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
                    assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.RESOURCE_NOT_FOUND);
                    assertThat(ex.getMessage()).isEqualTo("AI chat conversation not found");
                });
    }

    private AiChatMessage message(String conversationId, AiChatRole role, String content, Instant createdAt) {
        AiChatMessage message = new AiChatMessage();
        message.setConversationId(conversationId);
        message.setRole(role);
        message.setContent(content);
        message.setCreatedAt(createdAt);
        return message;
    }
}

package com.wellness.wellnessappbackend.ai.chat;

import com.wellness.wellnessappbackend.ai.AiClient;
import com.wellness.wellnessappbackend.ai.chat.dto.AiChatRequest;
import com.wellness.wellnessappbackend.ai.chat.dto.AiChatConversationDto;
import com.wellness.wellnessappbackend.ai.chat.dto.AiChatResponse;
import com.wellness.wellnessappbackend.ai.chat.dto.PythonChatMessage;
import com.wellness.wellnessappbackend.ai.chat.dto.PythonChatRequest;
import com.wellness.wellnessappbackend.ai.chat.dto.PythonChatResponse;
import com.wellness.wellnessappbackend.exception.ApiException;
import com.wellness.wellnessappbackend.exception.ErrorCode;
import com.wellness.wellnessappbackend.user.AppUser;
import com.wellness.wellnessappbackend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

@Service
@RequiredArgsConstructor
public class AiChatService {

    private static final int HISTORY_LIMIT = 12;
    private static final int MESSAGE_PREVIEW_LENGTH = 160;

    private final AiClient aiClient;
    private final AiChatMapper aiChatMapper;
    private final AiChatMessageRepository aiChatMessageRepository;
    private final UserRepository userRepository;

    @Transactional
    public AiChatResponse chat(Long userId, AiChatRequest request) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorCode.RESOURCE_NOT_FOUND, "User not found"));
        String conversationId = resolveConversationId(request.conversationId());

        List<AiChatMessage> history = recentHistory(userId, conversationId);
        List<PythonChatMessage> aiHistory = new ArrayList<>(history.stream().map(aiChatMapper::toPythonMessage).toList());
        aiHistory.add(new PythonChatMessage("user", request.message().trim()));

        PythonChatResponse aiResponse = aiClient.chat(new PythonChatRequest(
                userId,
                request.message().trim(),
                aiHistory
        ));

        AiChatMessage userMessage = new AiChatMessage();
        userMessage.setUser(user);
        userMessage.setConversationId(conversationId);
        userMessage.setRole(AiChatRole.USER);
        userMessage.setContent(request.message().trim());

        AiChatMessage assistantMessage = new AiChatMessage();
        assistantMessage.setUser(user);
        assistantMessage.setConversationId(conversationId);
        assistantMessage.setRole(AiChatRole.ASSISTANT);
        assistantMessage.setContent(aiResponse.reply());
        assistantMessage.setModelName(aiResponse.modelName());

        aiChatMessageRepository.save(userMessage);
        AiChatMessage savedAssistantMessage = aiChatMessageRepository.save(assistantMessage);

        List<AiChatMessage> responseMessages = new ArrayList<>(history);
        responseMessages.add(userMessage);
        responseMessages.add(savedAssistantMessage);

        return new AiChatResponse(
                conversationId,
                savedAssistantMessage.getContent(),
                aiResponse.requestId(),
                savedAssistantMessage.getModelName(),
                savedAssistantMessage.getCreatedAt(),
                responseMessages.stream().map(aiChatMapper::toDto).toList()
        );
    }

    @Transactional(readOnly = true)
    public Page<AiChatConversationDto> listConversations(Long userId, Pageable pageable) {
        return aiChatMessageRepository.findConversationIdsByUserIdOrderByLastMessageAtDesc(userId, pageable)
                .map(conversationId -> toConversationDto(userId, conversationId));
    }

    @Transactional(readOnly = true)
    public Page<AiChatMessage> getMessages(Long userId, String conversationId, Pageable pageable) {
        String normalizedConversationId = normalizeConversationId(conversationId);
        if (!aiChatMessageRepository.existsByUserIdAndConversationId(userId, normalizedConversationId)) {
            throw new ApiException(HttpStatus.NOT_FOUND, ErrorCode.RESOURCE_NOT_FOUND, "AI chat conversation not found");
        }
        return aiChatMessageRepository.findByUserIdAndConversationId(userId, normalizedConversationId, pageable);
    }

    private String resolveConversationId(String conversationId) {
        if (conversationId == null || conversationId.isBlank()) {
            return UUID.randomUUID().toString();
        }
        return normalizeConversationId(conversationId);
    }

    private String normalizeConversationId(String conversationId) {
        try {
            return UUID.fromString(conversationId.trim()).toString();
        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR, "conversationId must be a valid UUID");
        }
    }

    private List<AiChatMessage> recentHistory(Long userId, String conversationId) {
        List<AiChatMessage> newestFirst = aiChatMessageRepository.findByUserIdAndConversationIdOrderByCreatedAtDesc(
                userId,
                conversationId,
                PageRequest.of(0, HISTORY_LIMIT)
        );
        List<AiChatMessage> chronological = new ArrayList<>(newestFirst);
        Collections.reverse(chronological);
        return chronological;
    }

    private AiChatConversationDto toConversationDto(Long userId, String conversationId) {
        AiChatMessage firstMessage = aiChatMessageRepository
                .findFirstByUserIdAndConversationIdOrderByCreatedAtAsc(userId, conversationId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorCode.RESOURCE_NOT_FOUND, "AI chat conversation not found"));
        AiChatMessage lastMessage = aiChatMessageRepository
                .findFirstByUserIdAndConversationIdOrderByCreatedAtDesc(userId, conversationId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorCode.RESOURCE_NOT_FOUND, "AI chat conversation not found"));
        long messageCount = aiChatMessageRepository.countByUserIdAndConversationId(userId, conversationId);

        return new AiChatConversationDto(
                conversationId,
                firstMessage.getCreatedAt(),
                lastMessage.getCreatedAt(),
                messageCount,
                lastMessage.getRole().name(),
                preview(lastMessage.getContent())
        );
    }

    private String preview(String content) {
        if (content == null || content.length() <= MESSAGE_PREVIEW_LENGTH) {
            return content;
        }
        return content.substring(0, MESSAGE_PREVIEW_LENGTH) + "...";
    }
}

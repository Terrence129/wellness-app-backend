package com.wellness.wellnessappbackend.ai.chat;

import com.wellness.wellnessappbackend.ai.chat.dto.AiChatRequest;
import com.wellness.wellnessappbackend.ai.chat.dto.AiChatConversationDto;
import com.wellness.wellnessappbackend.ai.chat.dto.AiChatMessageDto;
import com.wellness.wellnessappbackend.ai.chat.dto.AiChatResponse;
import com.wellness.wellnessappbackend.common.ApiResponse;
import com.wellness.wellnessappbackend.common.PageResponse;
import com.wellness.wellnessappbackend.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

@RestController
@RequestMapping("/api/ai/chat")
@RequiredArgsConstructor
public class AiChatController {

    private final AiChatService aiChatService;
    private final AiChatMapper aiChatMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<AiChatResponse>> chat(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody AiChatRequest request
    ) {
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noStore())
                .body(ApiResponse.ok("AI chat response generated successfully", aiChatService.chat(principal.getId(), request)));
    }

    @GetMapping("/conversations")
    public ResponseEntity<ApiResponse<PageResponse<AiChatConversationDto>>> conversations(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        PageRequest pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 100));
        PageResponse<AiChatConversationDto> data = PageResponse.from(
                aiChatService.listConversations(principal.getId(), pageable),
                List.of("lastMessageAt,desc")
        );
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noStore())
                .body(ApiResponse.ok("Success", data));
    }

    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<ApiResponse<PageResponse<AiChatMessageDto>>> messages(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable String conversationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "createdAt,asc") String sort
    ) {
        Sort springSort = AiChatMessageSort.parse(sort);
        PageRequest pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 100), springSort);
        PageResponse<AiChatMessageDto> data = PageResponse.from(
                aiChatService.getMessages(principal.getId(), conversationId, pageable).map(aiChatMapper::toDto),
                AiChatMessageSort.describe(springSort)
        );
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noStore())
                .body(ApiResponse.ok("Success", data));
    }
}

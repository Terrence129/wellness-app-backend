package com.wellness.wellnessappbackend.ai.chat.dto;

import com.wellness.wellnessappbackend.ai.advice.dto.PythonAiLog;

import java.util.List;

public record PythonChatRequest(
        Long userId,
        String conversationId,
        String message,
        List<PythonChatMessage> history,
        List<PythonAiLog> recentLogs
) {
}

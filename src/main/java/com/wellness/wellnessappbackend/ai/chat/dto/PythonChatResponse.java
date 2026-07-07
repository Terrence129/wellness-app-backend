package com.wellness.wellnessappbackend.ai.chat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PythonChatResponse(
        String reply,
        String requestId,
        String modelName
) {
}

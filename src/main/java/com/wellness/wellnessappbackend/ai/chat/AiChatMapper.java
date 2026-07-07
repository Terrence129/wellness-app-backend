package com.wellness.wellnessappbackend.ai.chat;

import com.wellness.wellnessappbackend.ai.chat.dto.AiChatMessageDto;
import com.wellness.wellnessappbackend.ai.chat.dto.PythonChatMessage;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

@Component
public class AiChatMapper {

    public AiChatMessageDto toDto(AiChatMessage message) {
        return new AiChatMessageDto(
                message.getRole().name(),
                message.getContent(),
                message.getModelName(),
                message.getCreatedAt()
        );
    }

    public PythonChatMessage toPythonMessage(AiChatMessage message) {
        return new PythonChatMessage(message.getRole().name().toLowerCase(), message.getContent());
    }
}

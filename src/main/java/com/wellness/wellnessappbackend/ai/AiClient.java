package com.wellness.wellnessappbackend.ai;

import com.wellness.wellnessappbackend.ai.advice.dto.PythonAiRequest;
import com.wellness.wellnessappbackend.ai.advice.dto.PythonAiResponse;
import com.wellness.wellnessappbackend.ai.chat.dto.PythonChatRequest;
import com.wellness.wellnessappbackend.ai.chat.dto.PythonChatResponse;
import com.wellness.wellnessappbackend.exception.ApiException;
import com.wellness.wellnessappbackend.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class AiClient {

    private final RestClient restClient;

    public AiClient(RestClient.Builder restClientBuilder, @Value("${app.ai.base-url}") String baseUrl) {
        this.restClient = restClientBuilder.baseUrl(baseUrl).build();
    }

    public PythonAiResponse generateAdvice(PythonAiRequest request) {
        try {
            PythonAiResponse response = restClient.post()
                    .uri("/ai/wellness-advice")
                    .body(request)
                    .retrieve()
                    .body(PythonAiResponse.class);
            if (response == null || response.adviceText() == null || response.adviceText().isBlank()) {
                throw unavailable();
            }
            return response;
        } catch (RestClientException ex) {
            throw unavailable();
        }
    }

    public PythonChatResponse chat(PythonChatRequest request) {
        try {
            PythonChatResponse response = restClient.post()
                    .uri("/ai/chat")
                    .body(request)
                    .retrieve()
                    .body(PythonChatResponse.class);
            if (response == null || response.reply() == null || response.reply().isBlank()) {
                throw unavailable();
            }
            return response;
        } catch (RestClientException ex) {
            throw unavailable();
        }
    }

    private ApiException unavailable() {
        return new ApiException(HttpStatus.SERVICE_UNAVAILABLE, ErrorCode.AI_SERVICE_UNAVAILABLE, "AI service is unavailable");
    }
}

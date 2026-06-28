package com.wellness.wellnessappbackend.ai;

import com.wellness.wellnessappbackend.ai.dto.PythonAiRequest;
import com.wellness.wellnessappbackend.ai.dto.PythonAiResponse;
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

    private ApiException unavailable() {
        return new ApiException(HttpStatus.SERVICE_UNAVAILABLE, ErrorCode.AI_SERVICE_UNAVAILABLE, "AI service is unavailable");
    }
}

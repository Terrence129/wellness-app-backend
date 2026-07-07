package com.wellness.wellnessappbackend.ai;

import com.wellness.wellnessappbackend.ai.advice.dto.PythonAiRequest;
import com.wellness.wellnessappbackend.ai.advice.dto.PythonAiResponse;
import com.wellness.wellnessappbackend.ai.chat.dto.PythonChatRequest;
import com.wellness.wellnessappbackend.ai.chat.dto.PythonChatResponse;
import com.wellness.wellnessappbackend.exception.ApiException;
import com.wellness.wellnessappbackend.exception.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.time.Duration;
import java.util.List;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

@Component
public class AiClient {

    private static final Logger log = LoggerFactory.getLogger(AiClient.class);
    private static final String WELLNESS_ADVICE_PATH = "/ai/wellness-advice";
    private static final String CHAT_PATH = "/ai/chat";

    private final RestClient restClient;

    public AiClient(
            RestClient.Builder restClientBuilder,
            @Value("${app.ai.base-url}") String baseUrl,
            @Value("${app.ai.timeout-seconds}") long timeoutSeconds
    ) {
        int timeoutMillis = Math.toIntExact(Duration.ofSeconds(timeoutSeconds).toMillis());
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(timeoutMillis);
        requestFactory.setReadTimeout(timeoutMillis);

        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .defaultHeaders(headers -> {
                    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .build();
    }

    public PythonAiResponse generateAdvice(PythonAiRequest request) {
        try {
            PythonAiResponse response = restClient.post()
                    .uri(WELLNESS_ADVICE_PATH)
                    .body(request)
                    .retrieve()
                    .body(PythonAiResponse.class);
            if (response == null || response.adviceText() == null || response.adviceText().isBlank()) {
                throw unavailable();
            }
            return response;
        } catch (RestClientResponseException ex) {
            log.warn("AI service returned {} for {}: {}", ex.getStatusCode(), WELLNESS_ADVICE_PATH, ex.getResponseBodyAsString());
            throw unavailable();
        } catch (RestClientException ex) {
            log.warn("AI service call failed for {}", WELLNESS_ADVICE_PATH, ex);
            throw unavailable();
        }
    }

    public PythonChatResponse chat(PythonChatRequest request) {
        try {
            PythonChatResponse response = restClient.post()
                    .uri(CHAT_PATH)
                    .body(request)
                    .retrieve()
                    .body(PythonChatResponse.class);
            if (response == null || response.reply() == null || response.reply().isBlank()) {
                throw unavailable();
            }
            return response;
        } catch (RestClientResponseException ex) {
            log.warn("AI service returned {} for {}: {}", ex.getStatusCode(), CHAT_PATH, ex.getResponseBodyAsString());
            throw unavailable();
        } catch (RestClientException ex) {
            log.warn("AI service call failed for {}", CHAT_PATH, ex);
            throw unavailable();
        }
    }

    private ApiException unavailable() {
        return new ApiException(HttpStatus.SERVICE_UNAVAILABLE, ErrorCode.AI_SERVICE_UNAVAILABLE, "AI service is unavailable");
    }
}

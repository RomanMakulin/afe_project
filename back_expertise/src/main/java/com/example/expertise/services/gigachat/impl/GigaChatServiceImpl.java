package com.example.expertise.services.gigachat.impl;

import com.example.expertise.config.GigachatConfig;
import com.example.expertise.services.gigachat.GigaChatAuthService;
import com.example.expertise.services.gigachat.GigaChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для работы с API GigaChat
 */
@Service
public class GigaChatServiceImpl implements GigaChatService {

    private static final Logger log = LoggerFactory.getLogger(GigaChatServiceImpl.class);
    private final RestTemplate restTemplate;
    private final GigachatConfig gigachatConfig;
    private final GigaChatAuthService gigaChatAuthService;
    private final ObjectMapper objectMapper;

    public GigaChatServiceImpl(RestTemplate restTemplate,
                               GigachatConfig gigachatConfig,
                               GigaChatAuthService gigaChatAuthService,
                               ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.gigachatConfig = gigachatConfig;
        this.gigaChatAuthService = gigaChatAuthService;
        this.objectMapper = objectMapper;
    }

    @Override
    public String sendMessage(String message) {
        String token = gigaChatAuthService.getAuthToken();
        HttpHeaders headers = createHeaders(token);

        // Формируем тело запроса через объектную модель
        GigaChatRequest request = new GigaChatRequest(
                "GigaChat",
                false,
                0,
                List.of(
                        new GigaChatMessage("system", "Отвечай как научный сотрудник"),
                        new GigaChatMessage("user", message)
                )
        );

        try {
            String jsonBody = objectMapper.writeValueAsString(request);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    gigachatConfig.getApiUrl(), HttpMethod.POST, requestEntity, String.class);
            String jsonResponse = response.getBody();
            return extractContentFromJson(jsonResponse);
        } catch (JsonProcessingException e) {
            log.error("Ошибка при сериализации запроса в JSON: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("Ошибка при отправке запроса в GigaChat: {}", e.getMessage());
            return null;
        }
    }

    private String extractContentFromJson(String jsonResponse) throws JsonProcessingException {
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode choicesNode = rootNode.path("choices");
        if (choicesNode.isArray() && choicesNode.size() > 0) {
            return choicesNode.get(0).path("message").path("content").asText();
        }
        throw new IllegalStateException("Не удалось извлечь текст ответа из JSON: " + jsonResponse);
    }

    private HttpHeaders createHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        headers.set("X-Session-ID", UUID.randomUUID().toString());
        headers.set("X-Client-ID", gigachatConfig.getClientId());
        return headers;
    }

    // Вспомогательные классы для формирования запроса
    private record GigaChatRequest(String model, boolean stream, int update_interval, List<GigaChatMessage> messages) {}
    private record GigaChatMessage(String role, String content) {}
}
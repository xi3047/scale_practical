package org.example.scaleaidemo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LlmService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${llm.api.url:https://api.openai.com/v1/chat/completions}")
    private String apiUrl;

    @Value("${llm.api.key:}")
    private String apiKey;

    @Value("${llm.api.model:gpt-3.5-turbo}")
    private String model;

    public LlmService() {
        this.webClient = WebClient.builder().build();
        this.objectMapper = new ObjectMapper();
    }

    public String classifyText(String text, String column, List<String> categories) {
        try {
            String prompt = buildClassificationPrompt(text, column, categories);
            String response = callLlmApi(prompt);
            return extractClassificationFromResponse(response, categories);
        } catch (Exception e) {
            System.err.println("LLM classification failed: " + e.getMessage());
            return "Unknown";
        }
    }

    private String buildClassificationPrompt(String text, String column, List<String> categories) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Please classify the following ").append(column).append(" into one of these categories: ");
        prompt.append(String.join(", ", categories));
        prompt.append("\n\nText to classify: \"").append(text).append("\"");
        prompt.append("\n\nRespond with only the category name, nothing else.");
        return prompt.toString();
    }

    private String callLlmApi(String prompt) throws Exception {
        if (apiKey.isEmpty()) {
            throw new RuntimeException("LLM API key not configured");
        }

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("messages", List.of(
            Map.of("role", "user", "content", prompt)
        ));
        requestBody.put("max_tokens", 50);
        requestBody.put("temperature", 0.1);

        Mono<String> response = webClient.post()
            .uri(apiUrl)
            .header("Authorization", "Bearer " + apiKey)
            .header("Content-Type", "application/json")
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(String.class);

        return response.block();
    }

    private String extractClassificationFromResponse(String response, List<String> categories) throws Exception {
        JsonNode jsonResponse = objectMapper.readTree(response);
        JsonNode choices = jsonResponse.get("choices");

        if (choices != null && choices.size() > 0) {
            String content = choices.get(0).get("message").get("content").asText().trim();

            // Find the closest matching category
            for (String category : categories) {
                if (content.toLowerCase().contains(category.toLowerCase()) ||
                    category.toLowerCase().contains(content.toLowerCase())) {
                    return category;
                }
            }

            return content; // Return as-is if no exact match
        }

        return "Unknown";
    }
}
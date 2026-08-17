package com.ims.ai;

import com.ims.common.ApiResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

@RestController
@RequestMapping("/api/ai")
public class AiChatController {

    private final ChatClient chatClient;

    public AiChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @PostMapping("/chat")
    public ResponseEntity<ApiResponse<String>> chat(@RequestBody ChatRequest request) {
        try {
            String answer = chatClient
                    .prompt()
                    .system("You are an AI assistant for an Inventory Management System. " +
                            "Use available tools to fetch live data before answering. " +
                            "Be concise, specific, and always include actual numbers and product names.")
                    .user(request.prompt())
                    .toolNames("getStockBySku", "listAllProducts", "getLowStockAlerts", "getOrderCounts")
                    .call()
                    .content();

            return ResponseEntity.ok(ApiResponse.success("AI response", answer));
        } catch (RestClientException e) {
            String errorMsg = e.getMessage();
            if (errorMsg != null && (errorMsg.contains("authentication") || errorMsg.contains("extracting response"))) {
                return ResponseEntity.ok(ApiResponse.success(
                    "AI response",
                    "🔑 Groq API Key required! Please replace `gsk_demo_groq_key` with your free Groq API key in application.properties under `spring.ai.openai.api-key`."
                ));
            }
            return ResponseEntity.ok(ApiResponse.success(
                "AI response",
                "⚠️ AI Assistant error: " + errorMsg
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.success(
                "AI response",
                "⚠️ AI Assistant error: " + e.getMessage()
            ));
        }
    }

    public record ChatRequest(String prompt) {}
}
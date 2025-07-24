package com.example.demo1.zhidao;

import com.example.demo1.client.AgentClient;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;


@Service
public class LlmService {

    private final AgentClient agentClient;
    private static final Logger log = LoggerFactory.getLogger(LlmService.class);

    public LlmService(AgentClient agentClient) {
        this.agentClient = agentClient;
    }

    /**
     * 同步请求大模型（非流式）
     */
    public ChatResponse syncChat(ChatRequest request) {
        // 确保stream为false
        request.setStream(false);

        return agentClient.syncPost("/v1/chat/completions", request, ChatResponse.class);
    }

    /**
     * 流式请求大模型 - 返回Flux流
     */
    public Flux<ChatStreamResponse> streamChat(ChatRequest request) {
        // 设置stream为true
        request.setStream(true);

        return agentClient.streamPost("/v1/chat/completions", request, ChatStreamResponse.class)
                .filter(response -> response != null && response.getChoices() != null)
                .doOnNext(response -> log.debug("收到流式响应: {}", response))
                .doOnError(error -> log.error("流式请求出错: ", error))
                .doOnComplete(() -> log.debug("流式响应完成"));
    }

    /**
     * 流式请求并收集完整响应（适合需要完整内容的场景）
     */
    public Mono<String> streamChatAndCollect(ChatRequest request) {
        return streamChat(request)
                .map(response -> extractContent(response))
                .filter(content -> !content.isEmpty())
                .collectList()
                .map(contentList -> String.join("", contentList));
    }

    /**
     * 流式请求带超时控制
     */
    public Flux<ChatStreamResponse> streamChatWithTimeout(ChatRequest request, Duration timeout) {
        return streamChat(request)
                .timeout(timeout)
                .onErrorResume(TimeoutException.class, ex -> {
                    log.warn("流式请求超时，切换到同步模式");
                    return Flux.just(convertToStreamResponse(syncChat(request)));
                });
    }

    /**
     * 带重试机制的流式请求
     */
    public Flux<ChatStreamResponse> streamChatWithRetry(ChatRequest request, int maxRetries) {
        return streamChat(request)
                .retry(maxRetries)
                .onErrorResume(ex -> {
                    log.error("流式请求重试{}次后仍失败，降级到同步模式", maxRetries, ex);
                    return Flux.just(convertToStreamResponse(syncChat(request)));
                });
    }

    /**
     * 自定义头部的流式请求（比如API Key等）
     */
    public Flux<ChatStreamResponse> streamChatWithHeaders(ChatRequest request, Map<String, String> headers) {
        return agentClient.withHeaders("/v1/chat/completions", httpHeaders -> {
                    headers.forEach(httpHeaders::set);
                })
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(ChatStreamResponse.class);
    }

    /**
     * 提取响应内容的辅助方法
     */
    private String extractContent(ChatStreamResponse response) {
        if (response.getChoices() != null && !response.getChoices().isEmpty()) {
            ChatChoice choice = response.getChoices().get(0);
            if (choice.getDelta() != null && choice.getDelta().getContent() != null) {
                return choice.getDelta().getContent();
            }
        }
        return "";
    }

    /**
     * 转换同步响应为流式响应格式
     */
    private ChatStreamResponse convertToStreamResponse(ChatResponse syncResponse) {
        // 转换逻辑，将同步响应适配为流式响应格式
        ChatStreamResponse streamResponse = new ChatStreamResponse();
        // ... 转换逻辑
        return streamResponse;
    }

    // 请求和响应的数据类
    @Data
    public static class ChatRequest {
        private String model = "gpt-3.5-turbo";
        private List<ChatMessage> messages;
        private boolean stream = false;
        private Double temperature = 0.7;
        private Integer maxTokens = 1000;

        // constructors, getters, setters...
    }

    @Data
    public static class ChatMessage {
        private String role; // "user", "assistant", "system"
        private String content;

        public ChatMessage(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

    @Data
    public static class ChatResponse {
        private String id;
        private String object;
        private Long created;
        private String model;
        private List<ChatChoice> choices;
        private Usage usage;
    }

    @Data
    public static class ChatStreamResponse {
        private String id;
        private String object;
        private Long created;
        private String model;
        private List<ChatChoice> choices;
    }

    @Data
    public static class ChatChoice {
        private Integer index;
        private ChatMessage message;
        private ChatMessage delta; // 流式响应中使用delta
        private String finishReason;
    }

    @Data
    public static class Usage {
        private Integer promptTokens;
        private Integer completionTokens;
        private Integer totalTokens;
    }
}
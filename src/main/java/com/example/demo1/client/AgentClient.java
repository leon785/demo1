package com.example.demo1.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Consumer;


public class AgentClient {

    private final WebClient webClient;
    private final String baseUrl;

    public AgentClient(WebClient webClient, String baseUrl) {
        this.webClient = webClient;
        this.baseUrl = baseUrl;
    }

    /**
     * 同步GET请求
     * @param endpoint 端点路径
     * @param responseType 响应类型
     * @param params 查询参数
     * @return 响应结果
     */
    public <T> T syncGet(String endpoint, Class<T> responseType, Map<String, Object> params) {
        return asyncGet(endpoint, responseType, params).block();
    }

    /**
     * 异步GET请求
     * @param endpoint 端点路径
     * @param responseType 响应类型
     * @param params 查询参数
     * @return Mono响应
     */
    public <T> Mono<T> asyncGet(String endpoint, Class<T> responseType, Map<String, Object> params) {
        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(endpoint);
                    params.forEach(uriBuilder::queryParam);
                    return uriBuilder.build();
                }) // capture of ?
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(responseType);
    }

    /**
     * 同步POST请求
     * @param endpoint 端点路径
     * @param body 请求体
     * @param responseType 响应类型
     * @return 响应结果
     */
    public <T, R> T syncPost(String endpoint, R body, Class<T> responseType) {
        return asyncPost(endpoint, body, responseType).block();
    }

    /**
     * 异步POST请求
     * @param endpoint 端点路径
     * @param body 请求体
     * @param responseType 响应类型
     * @return Mono响应
     */
    public <T, R> Mono<T> asyncPost(String endpoint, R body, Class<T> responseType) {
        return webClient.post()
                .uri(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(responseType);
    }

    /**
     * 流式POST请求
     * @param endpoint 端点路径
     * @param body 请求体
     * @param responseType 响应类型
     * @return Flux流
     */
    public <T, R> Flux<T> streamPost(String endpoint, R body, Class<T> responseType) {
        return webClient.post()
                .uri(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .bodyValue(body)
                .retrieve()
                .bodyToFlux(responseType);
    }

    /**
     * 带自定义头的请求构建器
     * @param endpoint 端点路径
     * @param headerConfigure 头配置器
     * @return 请求构建器
     */
    public WebClient.RequestBodySpec withHeaders(String endpoint, Consumer<HttpHeaders> headerConfigure) {
        return webClient.post()
                .uri(endpoint) // capture of ?
                .headers(headerConfigure);
    }
}
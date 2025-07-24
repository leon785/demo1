package com.example.demo1.config;

import com.example.demo1.client.AgentClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import io.netty.channel.ChannelOption;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;
import java.time.Duration;


@Configuration
public class ClientConfig {
    /**
     * 创建并配置WebClient Bean
     * @return 配置好的WebClient实例
     */
    @Bean
    public WebClient webClient() {
        // 创建基础的HttpClient并配置超时参数
        HttpClient httpClient = HttpClient.create()
                // 设置响应超时20秒（从请求开始到完整接收响应的时间）
                .responseTimeout(Duration.ofMillis(20000))
                // 设置连接超时3秒（建立TCP连接的最大等待时间）
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000);

        // 构建WebClient实例（支持响应式处理）
        return WebClient.builder()
                // 使用Reactor Netty作为HTTP连接器
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                // 配置交换策略（流式处理优化）
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> {
                            // 保持默认编码器
                            configurer.defaultCodecs();
                            // 其他内容缓冲区（使用-1表示）
                            // 对于流式处理，应该采用数据显示设置大小的缓冲区
                            configurer.defaultCodecs().maxInMemorySize(-1);
                            // 增加HTTP编解码器缓冲区（可选）
                            configurer.defaultCodecs()
                                    .enableLoggingRequestDetails(true);
                        })
                        .build()) // 必须调用 build() 方法构建 ExchangeStrategies 实例
                .build();
    }

    /**
     * 创建AgentClient Bean
     * @param webClient WebClient实例
     * @return AgentClient实例
     */
    @Bean
    public AgentClient agentClient(WebClient webClient) {
        // 这里可以配置大模型的baseUrl
        return new AgentClient(webClient, "https://api.openai.com");
    }

}


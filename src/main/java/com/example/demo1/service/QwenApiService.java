package com.example.demo1.service;

import com.alibaba.fastjson2.JSONB;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.lang.System;
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;

import java.io.IOException;

@Service
public class QwenApiService implements IQwenApiService{

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    private static final String MODEL = "qwen_turbo";
//    private static final String API_KEY = "sk-da83f61f17e843249b3b887b27e90673";
    private static final String API_KEY = "sk-95cf32d80cee44cdb0a498e7a60a641d";
    private static final String API_URL = "https://dashscope-intl.aliyuncs.com/compatible-mode/v1/chat/completions";

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public GenerationResult callQwen(String prompt) throws ApiException, NoApiKeyException, InputRequiredException {

        Generation gen = new Generation();
        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content("You are a helpful assistant.")
                .build();
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(prompt)
                .build();
        GenerationParam param = GenerationParam.builder()
                // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
                .apiKey(API_KEY)
                // 此处以qwen-plus为例，可按需更换模型名称。模型列表：https://help.aliyun.com/zh/model-studio/getting-started/models
                .model("qwen-turbo")
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();

        return gen.call(param);
    }

    @Override
    public String callQwenByHttp(String prompt) throws IOException {
        // 构造请求体
        // 构造 JSON 请求体
        ObjectNode root = mapper.createObjectNode();
        root.put("model", MODEL);

        // 构建 messages 数组
        ArrayNode messages = mapper.createArrayNode();

        // system message
        ObjectNode systemMessage = mapper.createObjectNode();
        systemMessage.put("role", "system");
        systemMessage.put("content", "You are a helpful assistant.");
        messages.add(systemMessage);

        // user message
        ObjectNode userMessage = mapper.createObjectNode();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);
        messages.add(userMessage);

        root.set("messages", messages);
        root.put("temperature", 0.7); // 可选参数

        // 转成 JSON 字符串
        String jsonBody = mapper.writeValueAsString(root);
        System.out.println("请求 JSON：\n" + jsonBody);

        RequestBody body = RequestBody.create(
                jsonBody,
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String err = response.body() != null ? response.body().string() : "";
                throw new IOException("请求失败: " + response.code() + " - " + response.message() + "\n" + err);
            }

            return response.body().string(); // 正确返回 JSON
        }

//        // system message
//        ObjectNode systemMessage = mapper.createObjectNode();
//        systemMessage.put("role", "system");
//        systemMessage.put("content", "You are a helpful assistant.");
//        messages.add(systemMessage);
//
//        // user message
//        ObjectNode userMessage = mapper.createObjectNode();
//        userMessage.put("role", "user");
//        userMessage.put("content", prompt);
//        messages.add(userMessage);
//
//        root.set("messages", messages);
//        root.put("temperature", 0.7); // 可选参数
//        String requestBodyJson = """
//        {
//          "model": "qwen-turbo",
//          "messages": [
//            {"role": "system", "content": "You are a helpful assistant."},
//            {"role": "user", "content": "%s"}
//          ],
//          "temperature": 0.7
//        }
//        """.formatted(prompt);
//
//        RequestBody body = RequestBody.create(
//                requestBodyJson,
//                MediaType.parse("application/json")
//        );
//
//        Request request = new Request.Builder()
//                .url(API_URL)
//                .addHeader("Content-Type", "application/json")
//                .addHeader("Authorization", "Bearer " + API_KEY)
//                .post(body)
//                .build();
//
//        // 发送请求
//        try (Response response = client.newCall(request).execute()) {
//            if (!response.isSuccessful()) {
//                throw new IOException("调用失败: " + response.code() + " - " + response.message());
//            }
//
//            return response.body().string();  // 你自己定义的结果类
//        }
    }


}

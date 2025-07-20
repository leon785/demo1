package com.example.demo1.service;

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

    private static final String API_KEY = "sk-da83f61f17e843249b3b887b27e90673"; // 替换成你自己的 token

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


//        String jsonBody = "{\n" +
//                "  \"model\": \"qwen-turbo\",\n" +
//                "  \"input\": {\n" +
//                "    \"prompt\": \"" + prompt + "\"\n" +
//                "  },\n" +
//                "  \"parameters\": {\n" +
//                "    \"result_format\": \"text\"\n" +
//                "  }\n" +
//                "}";
//
//        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json"));
//
//        Request request = new Request.Builder()
//                .url(API_URL)
//                .header("Authorization", "Bearer " + API_KEY)
//                .post(body)
//                .build();
//
//        try (Response response = client.newCall(request).execute()) {
//            if (!response.isSuccessful()) {
//                return "模型接口调用失败: " + response.code();
//            }
//            return response.body().string();  // 返回原始字符串（下一步我们格式化）
//        }


    }


}

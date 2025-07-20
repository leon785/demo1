package com.example.demo1.controller;


import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.example.demo1.pojo.ResponseMessage;
import com.example.demo1.pojo.dto.PromptDto;
import com.example.demo1.service.IPromptFilterService;
import com.example.demo1.service.PromptFilterService;
import com.example.demo1.service.QwenApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;

import java.io.IOException;

@RestController
@RequestMapping("llm")
public class LlmController {

    @Autowired
    private IPromptFilterService promptFilter;
    @Autowired
    private QwenApiService qwenApiService;

    @PostMapping("/ask")
    public ResponseMessage<String> ask(@RequestBody PromptDto prompt) throws ApiException, NoApiKeyException, InputRequiredException {
        String p = prompt.getPrompt();
        String filteredP = promptFilter.filter(p);

        try {
            GenerationResult rawModelResponse = qwenApiService.callQwen(filteredP);
            return ResponseMessage.success(rawModelResponse.toString());

        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            return new ResponseMessage<>(500, e.getMessage(), null);
        }
    }
}

package com.example.demo1.service;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import okhttp3.*;

import java.io.IOException;


public interface IQwenApiService {
    public GenerationResult callQwen(String prompt) throws ApiException, NoApiKeyException, InputRequiredException ;
}

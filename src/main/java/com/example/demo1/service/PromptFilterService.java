package com.example.demo1.service;


import org.springframework.stereotype.Service;

@Service
public class PromptFilterService implements IPromptFilterService{
    @Override
    public String filter(String prompt) {
        if (prompt == null) {
            return "";
        }

        String res = prompt.trim();
        res = res.replaceAll("(?i)敏感词", "***");

        int maxLen = 500;
        if (res.length() > maxLen) {
            res = res.substring(0, maxLen);
        }

        return res;
    }

}

package com.example.demo1.config;

public class AgentClientPropertiesConfig {
    private String baseUrl = "";
    private int timeOut = 5000;

    public AgentClientPropertiesConfig(String baseUrl, int timeOut) {
        this.baseUrl = baseUrl;
        this.timeOut = timeOut;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}

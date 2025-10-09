package com.kcn.e_shop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ChatResponse {
    // Getters and setters
    private String response;
    private String sessionId;
    private boolean success;
    private String errorMessage;


    public ChatResponse(String response, boolean success) {
        this.response = response;
        this.success = success;
    }

    // Static factory methods
    public static ChatResponse success(String response) {
        return new ChatResponse(response, true);
    }

    public static ChatResponse error(String errorMessage) {
        ChatResponse response = new ChatResponse();
        response.setSuccess(false);
        response.setErrorMessage(errorMessage);
        return response;
    }

}
package com.kcn.e_shop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ChatRequest {
    private String message;
    private String sessionId;


    public ChatRequest(String message) {
        this.message = message;
    }

}

package com.kcn.e_shop.controller;

import com.kcn.e_shop.dto.ChatResponse;
import com.kcn.e_shop.dto.ChatRequest;
import com.kcn.e_shop.service.AiChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ai-chat")
public class AiChatController {

    private final AiChatService aiChatService;

//    @GetMapping("/")
//    public String chatPage() {
//        return "chat-ai";
//    }

    @PostMapping("/message")
    @ResponseBody
    public ChatResponse handleChatMessage(
            @RequestBody ChatRequest chatRequest,
            @AuthenticationPrincipal UserDetails userDetails) {

        return aiChatService.processMessage(chatRequest, userDetails);
    }

    @GetMapping("/health")
    @ResponseBody
    public ChatResponse healthCheck() {
        return aiChatService.healthCheck();
    }
}

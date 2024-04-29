package com.RWI.Nidhi.ChatBox;

import com.RWI.Nidhi.entity.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatMessageController {

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/user/{recipient}")
    public ChatMessage sendMessage(ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        chatService.sendMessage(chatMessage.getSender(), chatMessage.getRecipient(), chatMessage.getMessage());
        return chatMessage;
    }


}

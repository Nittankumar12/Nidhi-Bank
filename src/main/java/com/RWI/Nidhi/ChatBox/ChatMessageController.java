package com.RWI.Nidhi.ChatBox;

import com.RWI.Nidhi.entity.ChatMessage;
import com.RWI.Nidhi.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatMessageController {

    @Autowired
    private ChatService chatService;

    @MessageMapping("/conversation/{userId}/{recipientId}")
    @SendTo("/topic/conversation/{userId}/{recipientId}")
    public List<ChatMessage> getConversation(@PathVariable("userId") int userId, @PathVariable("recipientId") int recipientId) {
        User user = new User(userId, "User Name");
        User recipient = new User(recipientId, "Recipient Name");
        return chatService.getConversation(user, recipient);
    }

    @MessageMapping("/send")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        chatService.sendMessage(chatMessage);
        return chatMessage;
    }

}

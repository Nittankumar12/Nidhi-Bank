package com.RWI.Nidhi.ChatBox;

import com.RWI.Nidhi.entity.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    public void sendMessage(String sender, String recipient, String message) {
        LocalTime currTime = LocalTime.now();
        ChatMessage chatMessage = new ChatMessage(sender,recipient,message,currTime);
        chatRepository.save(chatMessage);
        messagingTemplate.convertAndSendToUser("/user/" ,recipient, chatMessage);
    }

    // ...
}

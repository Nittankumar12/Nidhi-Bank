package com.RWI.Nidhi.ChatBox;

import com.RWI.Nidhi.entity.ChatMessage;
import com.RWI.Nidhi.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ChatService {
    @Autowired
    private ChatRepository chatMessageRepository;

    public List<ChatMessage> getConversation(User user, User recipient) {
        List<ChatMessage> messages = new ArrayList<>();
        messages.addAll(chatMessageRepository.findBySenderAndRecipient(user, recipient));
        messages.addAll(chatMessageRepository.findBySenderAndRecipient(recipient, user));
        Collections.sort(messages, Comparator.comparing(ChatMessage::getCurrTime));
        return messages;
    }

    public void sendMessage(ChatMessage message) {
        chatMessageRepository.save(message);
    }
}

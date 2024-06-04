package com.RWI.Nidhi.ChatBox;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MyWebSocketHandler extends TextWebSocketHandler {

    // A thread-safe set to keep all connected WebSocket sessions
    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // Connection established
        System.out.println("New connection established: " + session.getId());
        sessions.add(session); // Add session to the set
    }


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // Handle incoming messages
        System.out.println("Received message: " + message.getPayload());

        // Broadcast the received message to all connected sessions
        for (WebSocketSession webSocketSession : sessions) {
            try {
                if (webSocketSession.isOpen()) {
                    webSocketSession.sendMessage(message);
                }
            } catch (IOException e) {
                System.err.println("Error sending message: " + e.getMessage());
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        // Remove the session when connection is closed
        sessions.remove(session);
        System.out.println("Connection closed: " + session.getId());
    }
}

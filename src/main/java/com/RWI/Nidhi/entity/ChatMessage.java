package com.RWI.Nidhi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.sql.Timestamp;


@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String sender;
    private String receiver;
    private MessageType type;

    public enum MessageType {
        CHAT, JOIN, LEAVE
    }

}
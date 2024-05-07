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
public class ChatMessage {

    private String text;
    private String sender;
    private String receiver;

}
package com.RWI.Nidhi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatBox {
    @Id
    private int chatId;
    private String userMessage;
    private String agentMessage;
    @UpdateTimestamp
    private Date notificationTime;
}

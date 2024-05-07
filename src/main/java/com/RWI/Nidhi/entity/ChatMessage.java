package com.RWI.Nidhi.entity;


import lombok.*;


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
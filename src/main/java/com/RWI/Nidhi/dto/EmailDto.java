package com.RWI.Nidhi.dto;

import lombok.Data;

@Data
public class EmailDto {
    private String to;
    private String subject;
    private String message;
}

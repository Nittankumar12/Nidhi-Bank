package com.RWI.Nidhi.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class EmailDto {
    private List<String> to;
    private String subject;
    private String message;
    private List<String> cc;
    private List<String> bcc;
    private List<MultipartFile> files;
}

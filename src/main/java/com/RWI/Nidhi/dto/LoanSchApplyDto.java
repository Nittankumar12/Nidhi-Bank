package com.RWI.Nidhi.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class LoanSchApplyDto {
    private String email;
    private MultipartFile sign;
    private MultipartFile video;
}

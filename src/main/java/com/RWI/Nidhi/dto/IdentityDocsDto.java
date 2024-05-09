package com.RWI.Nidhi.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class IdentityDocsDto {
    private Long aadharNumber;
    private String aadharImageFront;
    private String aadharImageBack;
    private String panNumber;
    private String panImage;
    private Long accountNumber;
    private String ifscCode;
    private String bankName;
    private String passbookImage;
    private Long voterIdNo;
    private String voterIdImageFront;
    private String voterIdImageBack;
    private String profilePhoto;

}

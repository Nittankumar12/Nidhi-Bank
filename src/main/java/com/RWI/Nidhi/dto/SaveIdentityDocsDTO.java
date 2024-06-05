package com.RWI.Nidhi.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data

public class SaveIdentityDocsDTO {
    private Long aadharNumber;
    private MultipartFile aadharImageFront;
    private MultipartFile aadharImageBack;
    private String panNumber;
    private MultipartFile panImage;
    private Long accountNumber;
    private String IFSC_Code;
    private String bankName;
    private MultipartFile passbookImage;
    private String voterIdNo;
    private MultipartFile voterIdImageFront;
  private MultipartFile voterIdImageBack;
    private MultipartFile profilePhoto;
}

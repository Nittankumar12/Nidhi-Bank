package com.nidhi.kyc.KYC.Service;

import org.springframework.web.multipart.MultipartFile;

public interface IdentityDocsService {

           String uploadFile(Long aadharNumber,
                             MultipartFile aadharImageFront,
                             MultipartFile aadharImageBack,
                             String panNumber,
                             MultipartFile panImage,
                             Long accountNumber,
                             String IFSC_Code,
                             String bankName,
                             MultipartFile passbookImage,
                             String voterIdNo,
                             MultipartFile voterIdImageFront,
                             MultipartFile voterIdImageBack,
                             MultipartFile profilePhoto);
}



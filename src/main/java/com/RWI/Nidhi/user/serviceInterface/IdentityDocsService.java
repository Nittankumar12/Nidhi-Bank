package com.RWI.Nidhi.user.serviceInterface;

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

    String getDownloadUrlForFront(Integer id);

    String getDownloadUrlForBack(Integer id);

    String getProfilePhoto(Integer id);

}




package com.nidhi.kyc.KYC.Service;

import com.nidhi.kyc.KYC.Enum.AdressDocumentType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AddressProofService {
    String uploadFile(String residentialAddress,
                      String permanentAddress,
                      String state,
                      String district,
                      String city,
                      long postalCode,
                      AdressDocumentType selectDocument,
                      MultipartFile docPhoto);

}

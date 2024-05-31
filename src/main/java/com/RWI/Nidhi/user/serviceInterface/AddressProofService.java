package com.RWI.Nidhi.user.serviceInterface;

//import com.nidhi.kyc.KYC.Enum.AddressDocumentType;

import com.RWI.Nidhi.entity.AddressProof;
import com.RWI.Nidhi.enums.AddressDocumentType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AddressProofService {
    String uploadFile(String residentialAddress,
                      String permanentAddress,
                      String state,
                      String district,
                      String city,
                      long postalCode,
                      AddressDocumentType selectDocument,
                      MultipartFile docPhoto);

    List<AddressProof> getAllAddress();

}

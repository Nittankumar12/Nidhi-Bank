package com.RWI.Nidhi.user.serviceInterface;

//import com.nidhi.kyc.KYC.Enum.AdressDocumentType;
import com.RWI.Nidhi.entity.AddressProof;
import com.RWI.Nidhi.entity.KycDetails;
import com.RWI.Nidhi.enums.AdressDocumentType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AddressProofService {
    String uploadFile(String residentialAddress,
                      String permanentAddress,
                      String state,
                      String district,
                      String city,
                      long postalCode,
                      AdressDocumentType selectDocument,
                      MultipartFile docPhoto);

     List<AddressProof> getAllAddress();

}

package com.RWI.Nidhi.user.serviceImplementation;

//import com.nidhi.kyc.KYC.Entity.AddressProof;
//import com.nidhi.kyc.KYC.Enum.AdressDocumentType;
//import com.nidhi.kyc.KYC.Repo.AddressRepo;
import com.RWI.Nidhi.entity.AddressProof;
import com.RWI.Nidhi.enums.AdressDocumentType;
import com.RWI.Nidhi.repository.AddressRepo;
import com.RWI.Nidhi.user.serviceInterface.AddressProofService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class AddressProofServiceImp implements AddressProofService {

    @Autowired
    private StorageService storageService;
    @Autowired
    private AddressRepo addressRepo;

    @Override
    public String uploadFile(String residentialAddress,
                             String permanentAddress,
                             String state,
                             String district,
                             String city,
                             long postalCode,
                             AdressDocumentType selectDocument,
                             MultipartFile docPhoto)  {

        AddressProof addressProof;
        try {
        String docPhotoUrl = storageService.uploadImage(docPhoto);

        addressProof = new AddressProof();
            addressProof.setResidentialAddress(residentialAddress);
            addressProof.setPermanentAddress(permanentAddress);
            addressProof.setState(state);
            addressProof.setDistrict(district);
            addressProof.setCity(city);
            addressProof.setPostalCode(postalCode);
            addressProof.setSelectDocument(selectDocument);
            addressProof.setDocPhoto(docPhotoUrl);
            addressRepo.save(addressProof);

        return null;
        }catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload";
        }
    }
}

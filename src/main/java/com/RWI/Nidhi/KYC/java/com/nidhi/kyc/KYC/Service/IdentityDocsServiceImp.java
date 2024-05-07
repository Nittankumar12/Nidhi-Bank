package com.nidhi.kyc.KYC.Service;

import com.nidhi.kyc.KYC.Dto.IdentityDocsDto;
import com.nidhi.kyc.KYC.Entity.IdentityDocs;
import com.nidhi.kyc.KYC.Repo.IdentityRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@Service
public class IdentityDocsServiceImp implements IdentityDocsService {

    @Autowired
    private IdentityRepo identityRepo;
    @Autowired
    private StorageService storageService;

    @Override
    @Transactional
    public String uploadFile(Long aadharNumber,
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
                             MultipartFile profilePhoto){

        IdentityDocs identityDocs;
        try {
            String aadharImageFrontUrl = storageService.uploadImage(aadharImageFront);
            String aadharImageBackUrl = storageService.uploadImage(aadharImageBack);
            String panImageUrl = storageService.uploadImage(panImage);
            String passbookImageUrl = storageService.uploadImage(passbookImage);
            String voterIdImageFrontUrl = storageService.uploadImage(voterIdImageFront);
            String voterIdImageBackUrl = storageService.uploadImage(voterIdImageBack);
            String profilePhotoUrl = storageService.uploadImage(panImage);

            identityDocs = new IdentityDocs();
            identityDocs.setAadharNumber(aadharNumber);
            identityDocs.setPanNumber(panNumber);
            identityDocs.setAccountNumber(accountNumber);
            identityDocs.setIFSC_Code(IFSC_Code);
            identityDocs.setBankName(bankName);
            identityDocs.setVoterIdNo(voterIdNo);
            identityRepo.save(identityDocs);

            return "Documents uploaded successfully";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload";
        }
    }

}

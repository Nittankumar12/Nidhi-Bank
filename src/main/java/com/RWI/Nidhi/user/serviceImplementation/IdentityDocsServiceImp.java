package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.dto.SaveIdentityDocsDTO;
import com.RWI.Nidhi.entity.IdentityDocs;
import com.RWI.Nidhi.repository.IdentityRepo;
import com.RWI.Nidhi.user.serviceInterface.IdentityDocsService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class IdentityDocsServiceImp implements IdentityDocsService {

    @Autowired
    private IdentityRepo identityRepo;
    @Autowired
    private StorageService storageService;

    @Override
    @Transactional
    public String uploadFile(SaveIdentityDocsDTO saveIdentityDocsDTO) {

        IdentityDocs identityDocs;
        try {
            String aadharImageFrontUrl = storageService.uploadImage(saveIdentityDocsDTO.getAadharImageFront());
            String aadharImageBackUrl = storageService.uploadImage(saveIdentityDocsDTO.getAadharImageBack());
            String panImageUrl = storageService.uploadImage(saveIdentityDocsDTO.getPanImage());
            String passbookImageUrl = storageService.uploadImage(saveIdentityDocsDTO.getPassbookImage());
            String voterIdImageFrontUrl = storageService.uploadImage(saveIdentityDocsDTO.getVoterIdImageFront());
            String voterIdImageBackUrl = storageService.uploadImage(saveIdentityDocsDTO.getVoterIdImageBack());
            String profilePhotoUrl = storageService.uploadImage(saveIdentityDocsDTO.getPanImage());

            identityDocs = new IdentityDocs();
            identityDocs.setAadharNumber(saveIdentityDocsDTO.getAadharNumber());
            identityDocs.setAadharImageFront(aadharImageFrontUrl);
            identityDocs.setAadharImageBack(aadharImageBackUrl);
            identityDocs.setPanNumber(saveIdentityDocsDTO.getPanNumber());
            identityDocs.setPanImage(panImageUrl);
            identityDocs.setAccountNumber(saveIdentityDocsDTO.getAccountNumber());
            identityDocs.setIFSC_Code(saveIdentityDocsDTO.getIFSC_Code());
            identityDocs.setBankName(saveIdentityDocsDTO.getBankName());
            identityDocs.setPassbookImage(passbookImageUrl);
            identityDocs.setVoterIdNo(saveIdentityDocsDTO.getVoterIdNo());
            identityDocs.setVoterIdImageFront(voterIdImageFrontUrl);
            identityDocs.setVoterIdImageBack(voterIdImageBackUrl);
            identityDocs.setProfilePhoto(profilePhotoUrl);
            identityRepo.save(identityDocs);

            return "Documents uploaded successfully";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload";
        }
    }

    @Override
    public String getDownloadUrlForFront(Integer id) {
        Optional<IdentityDocs> identityDocs = identityRepo.findById(id);
        return identityDocs.map(IdentityDocs::getAadharImageFront).orElse(null);
    }

    @Override
    public String getDownloadUrlForBack(Integer id) {
        Optional<IdentityDocs> identityDocs = identityRepo.findById(id);
        return identityDocs.map(IdentityDocs::getAadharImageBack).orElse(null);
    }

    @Override
    public String getProfilePhoto(Integer id) {
        Optional<IdentityDocs> identityDocs = identityRepo.findById(id);
        return identityDocs.map(IdentityDocs::getProfilePhoto).orElse(null);
    }

    @Override
    public String updateProfilePhoto(Integer id, MultipartFile profilePhoto) {
        IdentityDocs identityDocs1 = identityRepo
                .findById(id).orElseThrow(() -> new RuntimeException("doc id is not found"));
//        IdentityDocs identityDocs;
        try {
            String profilePhotoUrl = storageService.uploadImage(profilePhoto);
            {
//                identityDocs1 = new IdentityDocs();
                identityDocs1.setProfilePhoto(profilePhotoUrl);
                identityRepo.save(identityDocs1);
                return "Uploaded successfully:";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload";
        }
    }
<<<<<<< HEAD
=======

>>>>>>> f999b298a7752e57ce039e4294c0beb0f5aae629
    @Override
    public Integer getId(String email) {
        IdentityDocs identityDocs = identityRepo.findByEmail(email);
        return identityDocs != null ? identityDocs.getId() : null;
    }
}
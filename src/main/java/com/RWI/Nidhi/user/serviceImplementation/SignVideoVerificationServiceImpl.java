package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.entity.SignVideoVerification;
import com.RWI.Nidhi.repository.SignVideoVerificationRepo;
import com.RWI.Nidhi.user.serviceInterface.SignVideoVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class SignVideoVerificationServiceImpl implements SignVideoVerificationService {

    @Autowired
    private StorageService storageService;
    @Autowired
    private SignVideoVerificationRepo verification;
    @Override
    public String uploadImage(MultipartFile sign, MultipartFile video) {
        SignVideoVerification signVideoVerification;
        try {
            String signUrl = storageService.uploadImage(sign);
            String videoUrl = storageService.uploadImage(video);

            signVideoVerification = new SignVideoVerification();
            signVideoVerification.setSign(signUrl);
            signVideoVerification.setVideo(videoUrl);
            verification.save(signVideoVerification);

            return "Documents uploaded successfully";
        }catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload";
        }

    }

    @Override
    public List<SignVideoVerification> getAll() {
        return verification.findAll();
    }
}

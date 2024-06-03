package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.entity.SignVideoVerification;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SignVideoVerificationService {
    String uploadImage(MultipartFile sign,
                       MultipartFile video);

    List<SignVideoVerification> getAll();

    String uploadOnlyImage(MultipartFile sign);
    String uploadOnlyVideo(MultipartFile video);
}

package com.RWI.Nidhi.user.serviceInterface;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface EmailService {
    //send email to Multiple person
    void sendEmail(List<String> toList, String subject, String message,
                   List<String> ccList, List<String> bccList, List<MultipartFile> files);
}

package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.user.serviceImplementation.EmailServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private EmailServiceImplementation emailService;

    @PostMapping("/send-with-multiple-file")
    public ResponseEntity<String> sendEmailWithAttachments(@RequestParam("to") List<String> toList, @RequestParam("subject") String subject,
                                                           @RequestParam("message") String message, @RequestParam("cc") List<String> ccList,
                                                           @RequestParam("bcc") List<String> bccList, @RequestParam("files") List<MultipartFile> files) {
        try {
            emailService.sendEmail(toList, subject, message, ccList, bccList, files);
            return ResponseEntity.ok("Email sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email: " + e.getMessage());
        }
    }
}

package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.dto.EmailDto;
import com.RWI.Nidhi.user.serviceImplementation.EmailServiceImplementation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
    public ResponseEntity<String> sendEmailWithAttachments(@RequestBody EmailDto emailDto) {
        try {
            emailService.sendEmail(emailDto.getTo(),emailDto.getSubject(),emailDto.getMessage(),emailDto.getCc(),emailDto.getBcc(),emailDto.getFiles());
            return ResponseEntity.ok("Email sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email: " + e.getMessage());
        }
    }
}

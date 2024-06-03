package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.entity.SignVideoVerification;
import com.RWI.Nidhi.user.serviceImplementation.SignVideoVerificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("verification")
public class SignVideoVerificationController {

    @Autowired
    private SignVideoVerificationServiceImpl service;

    @PostMapping("/signVideoVerification")
    public ResponseEntity<String> uploadFile(@RequestParam("sign") MultipartFile sign,
                                             @RequestParam("video") MultipartFile video) {
        return ResponseEntity.ok(service.uploadImage(sign, video));
    }

    @GetMapping("/getAll")
    public ResponseEntity<Object> getAll() {
        List<SignVideoVerification> verification = service.getAll();
        return new ResponseEntity<>(verification, HttpStatus.OK);
    }

    @PostMapping("/sign")
    public ResponseEntity<String> uploadOnlyImage(@RequestParam("sign")MultipartFile sign){
        return ResponseEntity.ok(service.uploadOnlyImage(sign));
    }

    @PostMapping("/video")
    public ResponseEntity<String> uploadOnlyVideo(@RequestParam("video")MultipartFile video){
        return ResponseEntity.ok(service.uploadOnlyVideo(video));
    }

}

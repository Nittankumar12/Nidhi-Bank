package com.RWI.Nidhi.user.controller;

//import com.nidhi.kyc.KYC.Dto.IdentityDocsDto;
//import com.nidhi.kyc.KYC.Service.IdentityDocsService;

import com.RWI.Nidhi.user.serviceInterface.IdentityDocsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/kyc")
public class IdentityDocsController {

    @Autowired
    private IdentityDocsService identityDocsService;

    @PostMapping("/identityDocs")
    public ResponseEntity<String> saveIdentityDocs(@RequestParam("aadharNumber") Long aadharNumber,
                                                   @RequestParam("aadharImageFront") MultipartFile aadharImageFront,
                                                   @RequestParam("aadharImageBack") MultipartFile aadharImageBack,
                                                   @RequestParam("panNumber") String panNumber,
                                                   @RequestParam("panImage") MultipartFile panImage,
                                                   @RequestParam("accountNumber") Long accountNumber,
                                                   @RequestParam("IFSC_Code") String IFSC_Code,
                                                   @RequestParam("bankName") String bankName,
                                                   @RequestParam("passbookImage") MultipartFile passbookImage,
                                                   @RequestParam("voterIdNo") String voterIdNo,
                                                   @RequestParam("voterIdImageFront") MultipartFile voterIdImageFront,
                                                   @RequestParam("voterIdImageBack") MultipartFile voterIdImageBack,
                                                   @RequestParam("profilePhoto") MultipartFile profilePhoto) {
        return ResponseEntity.ok((identityDocsService.uploadFile(aadharNumber, aadharImageFront,
                aadharImageBack, panNumber,
                panImage, accountNumber,
                IFSC_Code, bankName,
                passbookImage, voterIdNo,
                voterIdImageFront, voterIdImageBack,
                profilePhoto)));
    }

    @GetMapping("/front/image-url/{id}")
    public ResponseEntity<String> getImageUrl(@PathVariable Integer id) {
        String string = identityDocsService.getDownloadUrlForFront(id);
        return new ResponseEntity<>(string, HttpStatus.OK);
    }


    @GetMapping("/back/image-url/{docId}")
    public ResponseEntity<String> getAdharImageBack(@PathVariable Integer id) {
        String string = identityDocsService.getDownloadUrlForBack(id);
        return new ResponseEntity<>(string, HttpStatus.OK);
    }

    @GetMapping("/ProfilePhoto/image-url/{docId}")
    public ResponseEntity<String> getProfilePhoto(@PathVariable Integer id) {
        String string = identityDocsService.getProfilePhoto(id);
        return new ResponseEntity<>(string, HttpStatus.OK);
    }
}

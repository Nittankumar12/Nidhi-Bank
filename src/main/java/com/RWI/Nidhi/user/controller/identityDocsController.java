package com.nidhi.kyc.KYC.Controller;

import com.nidhi.kyc.KYC.Dto.IdentityDocsDto;
import com.nidhi.kyc.KYC.Service.IdentityDocsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/kyc")
public class identityDocsController {

    @Autowired
    private IdentityDocsService identityDocsService;

    @PostMapping("/identitydocs")
    public ResponseEntity<String> saveIdentityDocs(@RequestParam("aadharNumbet") Long aadharNumber,
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
        return  ResponseEntity.ok((identityDocsService.uploadFile( aadharNumber,
                                                                   aadharImageFront,
                                                                   aadharImageBack,
                                                                   panNumber,
                                                                   panImage,
                                                                   accountNumber,
                                                                   IFSC_Code,
                                                                   bankName,
                                                                   passbookImage,
                                                                   voterIdNo,
                                                                   voterIdImageFront,
                                                                   voterIdImageBack,
                                                                   profilePhoto)));

    }

}


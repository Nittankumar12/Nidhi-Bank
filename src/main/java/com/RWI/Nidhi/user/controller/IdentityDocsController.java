package com.RWI.Nidhi.user.controller;

//import com.nidhi.kyc.KYC.Dto.IdentityDocsDto;
//import com.nidhi.kyc.KYC.Service.IdentityDocsService;

import com.RWI.Nidhi.dto.SaveIdentityDocsDTO;
import com.RWI.Nidhi.user.serviceInterface.IdentityDocsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@CrossOrigin("*")
@RestController
@RequestMapping("/kyc")
public class IdentityDocsController {

    @Autowired
    private IdentityDocsService identityDocsService;

    @PostMapping("/identityDocs")
    public ResponseEntity<String> saveIdentityDocs(@ModelAttribute SaveIdentityDocsDTO saveIdentityDocsDTO) {
        return ResponseEntity.ok((identityDocsService.uploadFile(saveIdentityDocsDTO)));
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

    @PutMapping("/updateProfilePhoto/{id}")
    public ResponseEntity<Object> updateProfile(@PathVariable Integer id,
                                                @RequestParam("profilePhoto") MultipartFile profilePhoto){
        String url = identityDocsService.updateProfilePhoto(id,profilePhoto);
        return  new ResponseEntity<>(url,HttpStatus.OK);
    }

}


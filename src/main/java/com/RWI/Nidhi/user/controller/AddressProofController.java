package com.RWI.Nidhi.user.controller;

//import com.nidhi.kyc.KYC.Enum.AddressDocumentType;
import com.RWI.Nidhi.entity.AddressProof;
import com.RWI.Nidhi.enums.AddressDocumentType;
//import com.nidhi.kyc.KYC.Service.AddressProofServiceImp;
import com.RWI.Nidhi.user.serviceImplementation.AddressProofServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/kyc")
public class AddressProofController {

    private AddressProofServiceImp addressProofServiceImp;

    @PostMapping("/address")
    public ResponseEntity<String> saveAddressDocs(@RequestParam("residentialAddress") String residentialAddress,
                                                   @RequestParam("permanentAddress") String permanentAddress,
                                                   @RequestParam("state") String state,
                                                   @RequestParam("district") String district,
                                                   @RequestParam("city") String city,
                                                   @RequestParam("postalCode") long postalCode,
                                                   @RequestParam("selectDocument") AddressDocumentType selectDocument,
                                                   @RequestParam("docPhoto") MultipartFile docPhoto){

        return   ResponseEntity.ok((addressProofServiceImp.uploadFile(residentialAddress,
                                                                      permanentAddress,
                                                                      state,
                                                                      district,
                                                                      city,
                                                                      postalCode,
                                                                      selectDocument,
                                                                      docPhoto)));
    }


    @GetMapping("/get/address")
    public ResponseEntity<Object> getAddressDetails(){
        List<AddressProof> addressProof = addressProofServiceImp.getAllAddress();
        return new ResponseEntity<>(addressProof, HttpStatus.OK);
    }
}

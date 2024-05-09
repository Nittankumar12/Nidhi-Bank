package com.RWI.Nidhi.user.controller;

//import com.nidhi.kyc.KYC.Enum.AdressDocumentType;
import com.RWI.Nidhi.enums.AdressDocumentType;
//import com.nidhi.kyc.KYC.Service.AddressProofServiceImp;
import com.RWI.Nidhi.user.serviceImplementation.AddressProofServiceImp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
                                                   @RequestParam("selectDocument") AdressDocumentType selectDocument,
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
}

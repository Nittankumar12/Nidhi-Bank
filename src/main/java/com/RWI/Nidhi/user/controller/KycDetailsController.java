package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.dto.KycDetailsDto;
import com.RWI.Nidhi.dto.ResponseKycDto;
import com.RWI.Nidhi.entity.KycDetails;
import com.RWI.Nidhi.user.serviceInterface.KycDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kyc")
//@CrossOrigin(origins = "http://localhost:8081/")
public class KycDetailsController {

    @Autowired
    private KycDetailsService kycDetailsService;

    //    @CrossOrigin(origins = "*")
    @PostMapping("/kycdetails")
    public ResponseEntity<KycDetailsDto> saveKycDetails(@RequestBody KycDetailsDto kycDetailsDTO) {
        KycDetailsDto savedKycDetailsDTO = kycDetailsService.saveKycDetails(kycDetailsDTO);
        if(savedKycDetailsDTO==null)return new ResponseEntity<>(savedKycDetailsDTO,HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity<>(savedKycDetailsDTO, HttpStatus.CREATED);
    }

    @GetMapping("/details/{userEmail}")
    public ResponseEntity<Object> getDetails(@RequestParam String userEmail) {
        KycDetails kycDetails = kycDetailsService.getDetailsByUserEmail(userEmail);
        return new ResponseEntity<>(kycDetails, HttpStatus.OK);
    }

    @GetMapping("/kycId/{id}")
    public ResponseEntity<ResponseKycDto> getSomeDetails(@PathVariable("id") Long kycId) {
        ResponseKycDto kycDetails = kycDetailsService.getSomeDetails(kycId);
        return new ResponseEntity<>(kycDetails, HttpStatus.OK);
    }

    @GetMapping("/getall")
    public ResponseEntity<Object> getAllKyc() {
        List<KycDetails> kycDetails = kycDetailsService.getAll();
        return new ResponseEntity<>(kycDetails, HttpStatus.OK);
    }
}

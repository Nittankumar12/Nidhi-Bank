package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.dto.SchemeApplyDTO;
import com.RWI.Nidhi.dto.SchemeInfoDto;
import com.RWI.Nidhi.user.serviceImplementation.SchemeServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scheme")
public class SchemeController {
    @Autowired
    SchemeServiceImplementation schemeService;
    @PostMapping("/apply")
    ResponseEntity<?> applyForScheme(@RequestBody SchemeApplyDTO schemeApplyDTO){
        return schemeService.addScheme(schemeApplyDTO);
    }
    @GetMapping("/SchemeInfo/{email}")
    ResponseEntity<?> getSchemeInfo(@PathVariable String email){
        return schemeService.getSchemeInfo(email);
    }
    @PutMapping("/monthlyDeposit/{email}")
    ResponseEntity<?> monthlyDeposit(@PathVariable String email){
        return schemeService.monthlyDeposit(email);
    }
    @GetMapping("/loanInfo/{email}")
    ResponseEntity<?> getSchemeLoanInfo(@PathVariable String email){
        return schemeService.getSchemeLoanInfo(email);
    }
    @PutMapping("/applyLoan/{email}")
    ResponseEntity<?> applyForSchemeLoan(@PathVariable String email){
        return schemeService.applyForSchemeLoan(email);
    }

}

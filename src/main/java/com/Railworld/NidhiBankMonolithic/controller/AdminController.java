package com.Railworld.NidhiBankMonolithic.controller;

import com.Railworld.NidhiBankMonolithic.dto.AdminDto;
import com.Railworld.NidhiBankMonolithic.model.Application;
import com.Railworld.NidhiBankMonolithic.model.LoanStatus;
import com.Railworld.NidhiBankMonolithic.model.Member;
import com.Railworld.NidhiBankMonolithic.service.ApplicationService;
import com.Railworld.NidhiBankMonolithic.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin")
public class AdminController {
    @Autowired
    CompanyService companyService;


    @GetMapping("getMembers/{id}")
    public ResponseEntity<List<Member>> getMembers(@PathVariable int id, @RequestBody AdminDto adminDto){
        return new ResponseEntity<>(companyService.getMembers(id), HttpStatus.OK);
    }
    @GetMapping("applications")
    public ResponseEntity<List<Application>> getApplications(){
        return new ResponseEntity<>(companyService.getApplications(),HttpStatus.OK);
    }

    @PutMapping("updateLoanStatus/{accountId}/{loanStatus}")
    public ResponseEntity<String> updateLoanStatus(@PathVariable int accountId, @PathVariable String loanStatus){
        companyService.updateLoanStatus(accountId,loanStatus);
        return new ResponseEntity<>("updated to "+ loanStatus + "",HttpStatus.OK);
    }
    @DeleteMapping("application/delete/{accountId}")
    public ResponseEntity<String> deleteApplication(@PathVariable int accountId){
        companyService.deleteApplication(accountId);
        return new ResponseEntity<>("deleted",HttpStatus.OK);
    }
}

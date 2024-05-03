package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.entity.FixedDeposit;
import com.RWI.Nidhi.entity.Loan;
import com.RWI.Nidhi.user.serviceImplementation.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/UserDetails")
public class UserDetailsController {
    @Autowired
    private UserDetailsServiceImpl service;

    @GetMapping("/getLoanDetails/{userId}")
    public Loan getLoanDetails(@PathVariable int UserId) throws UserPrincipalNotFoundException {
        return service.getLoanDetailsByUserId(UserId);
    }

    @GetMapping("/getFdDetails/{userId}")
    public List<FixedDeposit> getFdDetails(@PathVariable("userId") int UserId) throws UserPrincipalNotFoundException {
        return service.getFixedDepositDetailsByUserId(UserId);
    }

}

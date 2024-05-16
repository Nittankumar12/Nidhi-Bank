package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.enums.LoanType;
import com.RWI.Nidhi.user.serviceImplementation.EmiCalculatorServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Calculator")
public class EmiCalculatorController {

    @Autowired
    private EmiCalculatorServiceImplementation service;

    @PostMapping("/emi")
    public double[] calculateEmi(@RequestParam("principle") double principle, @RequestParam("loanType") LoanType loanType, @RequestParam("time") int time) {
        return service.calculateEMI(principle, loanType, time);
    }
}

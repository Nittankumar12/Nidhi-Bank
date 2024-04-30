package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.dto.RdDto;
import com.RWI.Nidhi.entity.FixedDeposit;
import com.RWI.Nidhi.entity.RecurringDeposit;
import com.RWI.Nidhi.user.serviceInterface.UserRdServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rd")
public class RdController {
    @Autowired
    private UserRdServiceInterface service;

    @PostMapping("/createRd")
    public RecurringDeposit createRd(@RequestBody RdDto rdDto) {
        return service.createRd(rdDto);
    }

    @DeleteMapping("/delete/{id}")
    public void closeRd(@PathVariable("id") int rdId) {
        System.out.println(rdId);
        service.closeRd(rdId);
    }
}

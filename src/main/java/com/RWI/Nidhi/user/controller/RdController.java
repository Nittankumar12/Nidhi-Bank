package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.dto.RdDto;
import com.RWI.Nidhi.entity.RecurringDeposit;
import com.RWI.Nidhi.user.serviceInterface.UserRdServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fd")
public class RdController {
    @Autowired
    private UserRdServiceInterface service;

    @PostMapping("/createFd")
    public RecurringDeposit createRd(@RequestBody RdDto rdDto) {
        return service.createRd(rdDto);
    }
}

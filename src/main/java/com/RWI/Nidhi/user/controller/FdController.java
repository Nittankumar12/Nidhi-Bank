package com.RWI.Nidhi.user.controller;


import com.RWI.Nidhi.dto.FdDto;
import com.RWI.Nidhi.entity.FixedDeposit;
import com.RWI.Nidhi.user.serviceInterface.UserFdServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fd")
public class FdController {
    @Autowired
    private UserFdServiceInterface service;

    @PostMapping("/createFd")
    public FixedDeposit createFd(@RequestBody FdDto fdDto) {
        return service.createFd(fdDto);
    }

    @DeleteMapping("/{id}")
    public void closeFd(@PathVariable int fdId) {
        service.closeFd(fdId);
    }
}
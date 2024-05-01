package com.RWI.Nidhi.user.controller;


import com.RWI.Nidhi.dto.FdDto;
import com.RWI.Nidhi.entity.FixedDeposit;
import com.RWI.Nidhi.user.serviceImplementation.UserFdServiceImplementation;
import com.RWI.Nidhi.user.serviceInterface.UserFdServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fd")
public class FdController {
    @Autowired
    private UserFdServiceImplementation serviceImplementation;

    @PostMapping("/createFd")
    public FixedDeposit createFd(@RequestBody FdDto fdDto) {
        return serviceImplementation.createFd(fdDto);
    }
    @DeleteMapping("/{id}")
    public void closeFd(@PathVariable int fdId) {
        serviceImplementation.closeFd(fdId);
    }
    @GetMapping("/getAllFd")
    public List<FixedDeposit> fixedDepositList(){
        return serviceImplementation.listOfFd();
    }


}

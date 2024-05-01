package com.RWI.Nidhi.user.controller;


import com.RWI.Nidhi.dto.FdDto;
import com.RWI.Nidhi.entity.FixedDeposit;
import com.RWI.Nidhi.user.serviceImplementation.UserFdServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fd")
public class FdController {
    @Autowired
    private UserFdServiceImplementation service;

    @PostMapping("/createFd")
    public FixedDeposit createFd(@RequestBody FdDto fdDto) { return service.createFd(fdDto);
    }

    @PutMapping("/update/{id}")
    public void closeFd(@PathVariable("id") int fdId) {
        System.out.println(fdId);
        service.closeFd(fdId);
    }

    @GetMapping("/allFds")
    public List<FixedDeposit> getAllFds() {
        return service.getAllFds();
    }

    @GetMapping("/find/{id}")
    public FixedDeposit findFdById(@PathVariable("id") int fdId) {
        return service.getFdById(fdId);
    }
}

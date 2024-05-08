package com.RWI.Nidhi.user.controller;


import com.RWI.Nidhi.dto.FdDto;
import com.RWI.Nidhi.dto.FdRequestDto;
import com.RWI.Nidhi.entity.FixedDeposit;
import com.RWI.Nidhi.user.serviceImplementation.UserFdServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fd")
public class FdController {
    @Autowired
    private UserFdServiceImplementation service;

    @PostMapping("/createFd")
    public ResponseEntity<Object> createFd(@RequestParam String agentEmail, @RequestParam String email, @RequestBody FdDto fdDto) {
        FdRequestDto fd = service.createFd(agentEmail, email, fdDto);
        return new ResponseEntity<>(fd, HttpStatus.OK);
    }

    @PutMapping("/delete/{id}")
    public void closeFd(@PathVariable("id") int fdId) {
        System.out.println(fdId);
        service.closeFd(fdId);
    }

    @GetMapping("/allFds")
    public List<FixedDeposit> getAllFds() {
        return service.getAllFds();
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<FdDto> findFdById(@RequestParam int fdId) {
        FdDto fdDto = service.getFdById(fdId);
        return new ResponseEntity<>(fdDto, HttpStatus.OK);
    }
}

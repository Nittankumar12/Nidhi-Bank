package com.RWI.Nidhi.user.controller;


import com.RWI.Nidhi.dto.FdDto;
import com.RWI.Nidhi.dto.FdResponseDto;
import com.RWI.Nidhi.dto.FdRequestDto;
import com.RWI.Nidhi.user.serviceImplementation.UserFdServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fd")
public class FdController {
    @Autowired
    private UserFdServiceImplementation service;

    @PostMapping("/createFd")
    public ResponseEntity<Object> createFd(@RequestParam String agentEmail, @RequestParam String email, @RequestBody FdDto fdDto) {
        FdResponseDto fd = service.createFd(agentEmail, email, fdDto);
        return new ResponseEntity<>(fd, HttpStatus.OK);
    }

    @PutMapping("/delete/{id}")
    public void closeFd(@PathVariable("id") int fdId) {
        System.out.println(fdId);
        service.closeFd(fdId);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<FdRequestDto> findFdById(@RequestParam int fdId) {
        FdRequestDto fdRequestDto = service.getFdById(fdId);
        return new ResponseEntity<>(fdRequestDto, HttpStatus.OK);
    }

    @GetMapping("/findAllFdsByEmail")
    public ResponseEntity<List<FdRequestDto>> findFdByEmail(@RequestParam String email) {
        List<FdRequestDto> fdRequestDtoList = service.getFdByEmail(email);
        return new ResponseEntity<>(fdRequestDtoList, HttpStatus.OK);
    }
}

package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.dto.FdDto;
import com.RWI.Nidhi.dto.FdRequestDto;
import com.RWI.Nidhi.dto.FdResponseDto;
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
    public ResponseEntity<Object> createFd( @RequestParam String email, @RequestBody FdDto fdDto) throws Exception {
        FdResponseDto fd = service.createFd( email, fdDto);
        if(fd == null) return new ResponseEntity<>("Either user account closed or error while creating fd", HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity<>(fd, HttpStatus.OK);
    }

    @PutMapping("/close/{id}")
    public ResponseEntity<String> closeFd(@PathVariable("id") int fdId) throws Exception {
        System.out.println(fdId);
        service.closeFd(fdId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/findById")
    public ResponseEntity<FdRequestDto> findFdById(@RequestParam int fdId) throws Exception {
        FdRequestDto fdRequestDto = service.getFdById(fdId);
        return new ResponseEntity<>(fdRequestDto, HttpStatus.OK);
    }

    @GetMapping("/findAllFdsByEmail")
    public ResponseEntity<List<FdRequestDto>> findFdByEmail(@RequestParam String email) throws Exception {
        List<FdRequestDto> fdRequestDtoList = service.getFdByEmail(email);
        return new ResponseEntity<>(fdRequestDtoList, HttpStatus.OK);
    }
}

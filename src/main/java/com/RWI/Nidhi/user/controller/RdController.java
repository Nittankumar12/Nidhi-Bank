package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.dto.RdDto;
import com.RWI.Nidhi.dto.RdRequestDto;
import com.RWI.Nidhi.entity.RecurringDeposit;
import com.RWI.Nidhi.user.serviceImplementation.UserRdServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/rd")
public class RdController {
    @Autowired
    private UserRdServiceImplementation service;

    @PostMapping("/createRd")
    public ResponseEntity<RdRequestDto> createRd(@RequestParam String agentEmail, @RequestParam String userEmail, @RequestBody RdDto rdDto) {
        RdRequestDto rd = service.createRd(agentEmail, userEmail, rdDto);
        return new ResponseEntity<>(rd, HttpStatus.OK);
    }

    @PutMapping("/delete/{id}")
    public void closeRd(@PathVariable("id") int rdId) {
        System.out.println(rdId);
        service.closeRd(rdId);
    }



    @GetMapping("/findById/{id}")
    public ResponseEntity<RdDto> findRdById(@RequestParam int rdId) {
        RdDto rdDto = service.getRdById(rdId);
        return new ResponseEntity<>(rdDto, HttpStatus.OK);
    }

    @GetMapping("/findAllRdsByEmail")
    public ResponseEntity<List<RdDto>> findRdByEmail(@RequestParam String email) {
        List<RdDto> rdDtoList = service.getRdByEmail(email);
        return new ResponseEntity<>(rdDtoList, HttpStatus.OK);
    }
}

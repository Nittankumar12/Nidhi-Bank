package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.dto.RdDto;
import com.RWI.Nidhi.dto.RdRequestDto;
import com.RWI.Nidhi.dto.RdResponseDto;
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
    public ResponseEntity<RdResponseDto> createRd(@RequestParam String agentEmail, @RequestParam String userEmail, @RequestBody RdDto rdDto) throws Exception {
        RdResponseDto rd = service.createRd(agentEmail, userEmail, rdDto);
        return new ResponseEntity<>(rd, HttpStatus.OK);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<String> closeRd(@PathVariable("id") int rdId) throws Exception {
        System.out.println(rdId);
        service.closeRd(rdId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/findById")
    public ResponseEntity<RdRequestDto> findRdById(@RequestParam int rdId) throws Exception {
        RdRequestDto responseDto = service.getRdById(rdId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/findAllRdsByEmail")
    public ResponseEntity<List<RdRequestDto>> findRdByEmail(@RequestParam String email) throws Exception {
        List<RdRequestDto> rdRequestDtoList = service.getRdByEmail(email);
        return new ResponseEntity<>(rdRequestDtoList, HttpStatus.OK);
    }

    @PutMapping("/sendMonthlyIncome")
    public ResponseEntity<?> sendMonthlyIncomeToUser(@RequestParam("rdId") int rdId) throws Exception {
        return service.sendMonthlyIncomeToUser(rdId);
    }
}

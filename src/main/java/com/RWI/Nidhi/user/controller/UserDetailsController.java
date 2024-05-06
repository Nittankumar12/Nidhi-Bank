package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.dto.FdResponseDto;
import com.RWI.Nidhi.dto.MisResponseDto;
import com.RWI.Nidhi.dto.RdResponseDto;
import com.RWI.Nidhi.user.serviceImplementation.UserDetailsServiceImpl;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/UserDetails")
public class UserDetailsController {
    @Autowired
    private UserDetailsServiceImpl service;


    @GetMapping("/getFdDetails/{userId}")
    public List<FdResponseDto> getFdDetails(@PathVariable("userId") int userId) {
        return service.getFixedDepositDetailsByUserId(userId);
    }
    @GetMapping("/getRdDetails/{userId}")
    public List<RdResponseDto> getRdDetails(@PathVariable("userId") int userId) {
        return service.getRecurringDepositDetailsByUserId(userId);
    }
    @GetMapping("/getMisDetails/{userId}")
    public List<MisResponseDto> getMisDetails(@PathVariable("userId") int userId) {
        return service.getMisDetailsByUserId(userId);
    }

}

package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.dto.FdResponseDto;
import com.RWI.Nidhi.dto.MisResponseDto;
import com.RWI.Nidhi.dto.RdResponseDto;
import com.RWI.Nidhi.user.serviceImplementation.UserServiceImpl;
import com.RWI.Nidhi.user.serviceImplementation.UserStatementServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/Statement")
public class UserStatementController {
    @Autowired
    private UserStatementServiceImpl service;
    @Autowired
    private UserServiceImpl userService;


//    @GetMapping("/getFdDetails/{email}")
//    public List<FdResponseDto> getFdDetails(@PathVariable("email") String email) {
//        if(userService.getByEmail(email).getAccounts() != null) {
//            return service.getFixedDepositDetailsByEmail(email);
//        }else {
//            return new ArrayList<FdResponseDto>();
//        }
//    }


    @GetMapping("/getFdDetails/{email}")
    public List<FdResponseDto> getFdDetails(@PathVariable("email") String email) {
        return service.getFixedDepositDetailsByEmail(email);
    }

    @GetMapping("/getRdDetails/{email}")
    public List<RdResponseDto> getRdDetails(@PathVariable("email") String email) {
        return service.getRecurringDepositDetailsByEmail(email);
    }

    @GetMapping("/getMisDetails/{email}")
    public List<MisResponseDto> getMisDetails(@PathVariable("email") String email) {
        return service.getMisDetailsByEmail(email);
    }

}

package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.dto.MisDto;
import com.RWI.Nidhi.dto.MisRequestDto;
import com.RWI.Nidhi.dto.MisResponseDto;
import com.RWI.Nidhi.user.serviceImplementation.UserMisServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mis")
public class MisController {
    @Autowired
    UserMisServiceImplementation misService;

    @PostMapping("/createMis")
    public ResponseEntity<?> createMis(@RequestParam String email, @RequestBody MisDto misDto) {
        System.out.println(misDto.getMisTenure().getInterestRate());
        System.out.println(misDto.getMisTenure().getTenure());
        MisResponseDto misResponseDto = misService.createMis(email, misDto);
        if(misResponseDto == null) return new ResponseEntity<>("either account closed or error while creating mis", HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity<>(misResponseDto, HttpStatus.OK);
    }

    @PutMapping("/closeMis")
    public Double closeMis(@RequestParam("misId") int misId) throws Exception {
        return misService.closeMis(misId);
    }
    @PutMapping("/sendMonthlyIncome")
    public ResponseEntity<?> sendMonthlyIncomeToUser(@RequestParam("misId") int misId) throws Exception{
        return misService.sendMonthlyIncomeToUser(misId);
    }

    @GetMapping("/findById")
    public ResponseEntity<MisRequestDto> findMisById(@RequestParam int misId)throws Exception {
        MisRequestDto misRequestDto = misService.getMisById(misId);
        return new ResponseEntity<>(misRequestDto, HttpStatus.OK);
    }

    @GetMapping("/findAllMisByEmail")
    public ResponseEntity<List<MisRequestDto>> findMisByEmail(@RequestParam String email) {
        List<MisRequestDto> misRequestDtoList = misService.getMisByEmail(email);
        return new ResponseEntity<>(misRequestDtoList, HttpStatus.OK);
    }
}

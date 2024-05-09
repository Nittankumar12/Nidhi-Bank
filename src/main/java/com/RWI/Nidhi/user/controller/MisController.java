package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.dto.FdDto;
import com.RWI.Nidhi.dto.MisDto;
import com.RWI.Nidhi.dto.MisRequestDto;
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
    public MisRequestDto createMis(@RequestParam String agentEmail, @RequestParam String email, @RequestBody MisDto misDto) {
        System.out.println(misDto.getMisTenure().getInterestRate());
        System.out.println(misDto.getMisTenure().getTenure());
        return misService.createMis(agentEmail, email, misDto);
    }

    @PutMapping("/closeMis")
    public Double closeMis(@RequestParam("misId") int misId) throws Exception {
        return misService.closeMis(misId);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<MisDto> findMisById(@RequestParam int misId) {
        MisDto misDto = misService.getMisById(misId);
        return new ResponseEntity<>(misDto, HttpStatus.OK);
    }

    @GetMapping("/findAllMisByEmail")
    public ResponseEntity<List<MisDto>> findMisByEmail(@RequestParam String email) {
        List<MisDto> misDtoList = misService.getMisByEmail(email);
        return new ResponseEntity<>(misDtoList, HttpStatus.OK);
    }
}

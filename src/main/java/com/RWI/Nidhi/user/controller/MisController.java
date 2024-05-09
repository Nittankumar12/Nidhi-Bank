package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.dto.MisDto;
import com.RWI.Nidhi.dto.MisRequestDto;
import com.RWI.Nidhi.user.serviceImplementation.UserMisServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}

package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.dto.RdDto;
import com.RWI.Nidhi.entity.RecurringDeposit;
import com.RWI.Nidhi.user.serviceImplementation.UserRdServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rd")
public class RdController {
    @Autowired
    private UserRdServiceImplementation service;

    @PostMapping("/createRd")
    public RecurringDeposit createRd(@RequestBody RdDto rdDto) {
        return service.createRd(rdDto);
    }

    @DeleteMapping("/delete/{id}")
    public void closeRd(@PathVariable("id") int rdId) {
        System.out.println(rdId);
        service.closeRd(rdId);
    }

    @GetMapping("/allRds")
    public List<RecurringDeposit> getAllRds() {
        return service.getAllRds();
    }

    @GetMapping("/find/{id}")
    public Optional<RecurringDeposit> findRdById(@PathVariable("id") int rdId) {
        return service.getRdById(rdId);
    }
}

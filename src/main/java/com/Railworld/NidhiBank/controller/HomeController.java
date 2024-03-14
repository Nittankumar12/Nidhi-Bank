package com.Railworld.NidhiBank.controller;

import com.Railworld.NidhiBank.dto.CompanyRequestDto;
import com.Railworld.NidhiBank.dto.UserRequestDto;
import com.Railworld.NidhiBank.model.Company;
import com.Railworld.NidhiBank.model.User;
import com.Railworld.NidhiBank.repo.CompanyRepository;
import com.Railworld.NidhiBank.service.CompanyService;
import com.Railworld.NidhiBank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.event.ComponentAdapter;

@RestController
@RequestMapping("bank/home")
public class HomeController {

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    @GetMapping("/company/{id}")
    public Company getCompany(@PathVariable int id){
        return companyService.getCompany(id);
    }

    @PostMapping("/company/register")
    public Company registerCompany(@RequestBody CompanyRequestDto company){
//        System.out.println(company.getCompanyBalance());
        System.out.println(company);
       return companyService.registerCompany(company);
    }

    @PostMapping("/user/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRequestDto user){
        userService.registerUser(user);
        return ResponseEntity.ok("User Registered");
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable int id){
        return userService.getUser(id);
    }
}

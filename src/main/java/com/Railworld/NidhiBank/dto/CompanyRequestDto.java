package com.Railworld.NidhiBank.dto;

import com.Railworld.NidhiBank.model.Member;
import com.Railworld.NidhiBank.model.User;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter
public class CompanyRequestDto {
    private String companyName;
    private String companyEmail;
    private String companyAddress;
    private Integer companyBalance;
//    private List<User> userList;
//    private List<Member> memberList;
}


/*
POSTMAN JSON

{
    "companyName": "Railworld India",
    "companyEmail": "railworld@gmail.om",
    "companyAddress": "744,Udyog Vihar,Gurugram",
    "companyBalance": 1000000
}


 */
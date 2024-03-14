package com.Railworld.NidhiBank.dto;

import com.Railworld.NidhiBank.model.Company;
import lombok.Data;

@Data
public class UserRequestDto {
    private Integer userId;
    private String userEmail;
    private String userName;
    private String userPassword;
    private Integer companyId;
}

/*
POSTMAN JSON
{
    "userEmail": "user@example.com",
    "userName": "John Doe",
    "userPassword": "password123",
    "companyId": 102
}

 */

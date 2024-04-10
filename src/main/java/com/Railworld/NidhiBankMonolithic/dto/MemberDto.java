package com.Railworld.NidhiBankMonolithic.dto;

import lombok.Data;

@Data
public class MemberDto {

    private UserDto userDto;
    private String name;
    private String email;
    private String date;
    private String address;
    private String phone;
    private String branch;
    private String accountType;
    private Double balance;

}

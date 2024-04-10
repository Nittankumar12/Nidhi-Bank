package com.Railworld.NidhiBankMonolithic.dto;

import lombok.Data;

@Data
public class UserDto {
    private String email;
    private String password;
    private Integer companyId;
}

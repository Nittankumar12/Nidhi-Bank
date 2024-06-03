package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.entity.PermanentAddress;
import com.RWI.Nidhi.entity.ResidentialAddress;
import com.RWI.Nidhi.enums.Education;
import lombok.Data;

@Data
public class UpdateUserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private UpdateAddressDTO residentialAddress;
    private UpdateAddressDTO permanentAddress;
    private Education education;
}



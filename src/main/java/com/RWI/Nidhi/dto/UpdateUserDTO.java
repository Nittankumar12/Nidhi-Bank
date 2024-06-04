package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.Education;
import com.RWI.Nidhi.enums.MartialStatus;
import lombok.Data;

@Data
public class UpdateUserDTO {
    private String firstName;
    private Long alternatePhnNo;
    private String lastName;
    private String oldEmail;
    private UpdateAddressDTO permanentAddress;
    private MartialStatus martialStatus;
    private Education education;
}



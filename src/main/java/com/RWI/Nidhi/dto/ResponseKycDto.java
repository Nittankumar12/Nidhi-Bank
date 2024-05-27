package com.RWI.Nidhi.dto;

//import com.nidhi.kyc.KYC.Enum.Education;
//import com.nidhi.kyc.KYC.Enum.Gender;

import com.RWI.Nidhi.enums.Education;
import com.RWI.Nidhi.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseKycDto {
    private String firstName;
    private String lastName;
    private String email;
    private Long phnNo;
    private Gender gender;
    private Date dateOfBirth;
    private Long alternatePhnNo;
    private Education education;
  //  private String permanentAddress;





}


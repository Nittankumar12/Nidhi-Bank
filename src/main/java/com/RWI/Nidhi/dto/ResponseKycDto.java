package com.nidhi.kyc.KYC.Dto;

import com.nidhi.kyc.KYC.Enum.Education;
import com.nidhi.kyc.KYC.Enum.Gender;
import lombok.*;

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


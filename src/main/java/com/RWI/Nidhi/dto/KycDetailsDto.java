package com.RWI.Nidhi.dto;

//import com.nidhi.kyc.KYC.Enum.*;
import com.RWI.Nidhi.enums.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KycDetailsDto {

        private String firstName;
        private String lastName;
        private String fatherName;
        private MartialStatus martialStatus;
        private String fatherLastName;
        private String email;
        private Long phnNo;
        private Education education;
        private Gender gender;
        private Date dateOfBirth;
        private Religion religion;
        private Nationality nationality;
        private Categories categories;
        private Long alternatePhnNo;
        private String nomineeFirstName;
        private String nomineeLastName;
        private Long nomineeContactNumber;
        private String relationWithNominee;
        private Occupation occupation;
        private Integer monthlyIncome;
        private Integer numberOfFamilyMembers;
        private KycStatus kycStatus;
        private String referralCode;
}




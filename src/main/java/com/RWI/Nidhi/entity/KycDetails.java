
package com.RWI.Nidhi.entity;

//import com.nidhi.kyc.KYC.Enum.*;
import com.RWI.Nidhi.enums.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

@Entity
@Data
@Validated
public class KycDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kycId;
    private String firstName;
    private String lastName;
    private String fatherName;
    private String fatherLastName;
    @Email(message = "Invalid email address")
    private String email;
    private Long phnNo;
    @Enumerated(value =EnumType.STRING)
    private Education education;
    @Enumerated(value = EnumType.STRING)
    private Gender gender;
    private Date dateOfBirth;
    @Enumerated(EnumType.STRING)
    private Religion religion;
    @Enumerated(EnumType.STRING)
    private Nationality nationality;
    @Enumerated(EnumType.STRING)
    private KycStatus kycStatus;
    @Enumerated(value =EnumType.STRING)
    private Categories categories;
    private Long alternatePhnNo;
    private String nomineeFirstName;
    private String nomineeLastName;
    private Long nomineeContactNumber;
    private String relationWithNominee;
    @Enumerated(value = EnumType.STRING)
    private Occupation occupation;
    @Enumerated(value = EnumType.STRING)
    private MartialStatus martialStatus;
    private Integer monthlyIncome;
    private Integer numberOfFamilyMembers;
    private String referralCode;
    @OneToOne
    @JoinColumn
    private User user;
    @OneToOne(mappedBy = "kycDetails", cascade = CascadeType.ALL)
    Address address;

}
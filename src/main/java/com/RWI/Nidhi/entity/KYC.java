package com.RWI.Nidhi.entity;

import com.RWI.Nidhi.enums.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class KYC {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int kycId;
    private int userId;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String aadharNumber;
    private String PANNumber;
    private String address;
    private int monthlyIncome;
    private String nomineeFirstName;
    private String nomineeLastName;
    private String nomineeContactNumber;
    private String relationWithNominee;
    private int numberOfFamilyMembers;
    @Lob
    private byte[] photograph;
    @Lob
    private byte[] aadharCardPhoto;
    @Lob
    private byte[] PANCardPhoto;
    @Enumerated(EnumType.STRING)
    private GenderType genderType;
    @Enumerated(EnumType.STRING)
    private GuardianType guardianType;
    @Enumerated(EnumType.STRING)
    private EducationType educationType;
    @Enumerated(EnumType.STRING)
    private NationalityType nationalityType;
    @Enumerated(EnumType.STRING)
    private OccupationType occupationType;
    @Enumerated(EnumType.STRING)
    private ReligionType religionType;
    @OneToOne
    @JoinColumn
    private User user;
    @OneToOne(mappedBy = "kyc", cascade = CascadeType.ALL)
    private PermanentAddress permanentAddress;
    @OneToOne(mappedBy = "kyc", cascade = CascadeType.ALL)
    private ResidentialAddress residentialAddress;
}

package com.RWI.Nidhi.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.awt.*;
import java.util.Date;

@Entity
@Data

public class IdentityDocs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Long aadharNumber;
    private String aadharImageFront;
    private String aadharImageBack;
    private String panNumber;
    private String panImage;
    private Long accountNumber;
    private String IFSC_Code;
    private String bankName;
    private String passbookImage;
    private String voterIdNo;
    private String voterIdImageFront;
    private String voterIdImageBack;
    private String profilePhoto;

}


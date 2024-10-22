package com.RWI.Nidhi.entity;

//import com.nidhi.kyc.KYC.Enum.AddressDocumentType;

import com.RWI.Nidhi.enums.AddressDocumentType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class AddressProof {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addId;
    private String residentialAddress;
    private String permanentAddress;
    private String state;
    private String district;
    private String city;
    private long postalCode;
    private AddressDocumentType selectDocument;
    private String docPhoto;
}

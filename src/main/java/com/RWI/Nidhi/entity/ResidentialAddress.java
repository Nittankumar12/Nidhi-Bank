package com.RWI.Nidhi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ResidentialAddress {
    @Id
    private int addressId;
    private String houseNumber;
    private String buildingName;
    private String locality;
    private String city;
    private String district;
    private String state;
    private int pinCode;
    private String landmark;

    @OneToOne
    @JoinColumn
    private KYC kyc;
}

package com.RWI.Nidhi.dto;

import lombok.Data;

@Data
public class UpdateAddressDTO {
    private String houseNumber;
    private String buildingName;
    private String locality;
    private String city;
    private String district;
    private String state;
    private int pinCode;
    private String landmark;
}
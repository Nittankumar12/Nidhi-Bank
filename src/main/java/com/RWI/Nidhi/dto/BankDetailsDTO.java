package com.RWI.Nidhi.dto;

import lombok.Data;

@Data
public class BankDetailsDTO {
    private String name;
    private long accNumber;
    private String bName;
    private String branchName;
    private String ifsc;
}

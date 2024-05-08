package com.RWI.Nidhi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankRequestDTO {
    
    private long accountNumber;
    private String branchName;
    private String bankName;
    private String ifsc;
}

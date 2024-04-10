package com.Railworld.NidhiBankMonolithic.dto;

import com.Railworld.NidhiBankMonolithic.model.Account;
import com.Railworld.NidhiBankMonolithic.model.Loan;
import com.Railworld.NidhiBankMonolithic.model.LoanStatus;
import lombok.Data;

@Data
public class LoanDto {
    private String loanType;
    private Double amount;
    private Integer accountId;
}

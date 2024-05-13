package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.enums.SchemeStatus;
import lombok.Data;

import java.time.LocalDate;
@Data
public class SchemeInfoDto {
    private double monthlyDepositAmount;
    private LocalDate startDate;
    private LocalDate nextEMIDate;
    private int tenure;
    private double totalDepositAmount;
    private double interestRate;
    private SchemeStatus sStatus;
    private Accounts accounts;
    private User user;
}

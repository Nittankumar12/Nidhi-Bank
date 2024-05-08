package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.Status;
import lombok.Data;

import java.time.LocalDate;
@Data
public class FdRequestDto {
    private String userName;
    private Integer amount;
    private LocalDate depositDate;
    private double interestRate;
    private int tenure;
    private double maturityAmount;
    private String nomineeName;
    private LocalDate maturityDate;
    private int compoundingFrequency;
    private Status fdStatus;
    private String agentName;
}

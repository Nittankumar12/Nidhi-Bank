package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class FdResponseDto {
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

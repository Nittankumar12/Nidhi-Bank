package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.CommissionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
@Data
public class CommissionDto {
    private Double commissionAmount;
    private double commissionRate;
    private String userName;
    private CommissionType commissionType;//Commission value - loan, mis, etc.
    private LocalDate commDate;
}

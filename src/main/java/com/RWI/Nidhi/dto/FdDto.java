package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.FdCompoundingFrequency;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FdDto {
    int amount;
    private String nomineeName;
    FdCompoundingFrequency fdCompoundingFrequency;
    int tenure;
}

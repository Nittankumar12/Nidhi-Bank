package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.FdCompoundingFrequency;
import lombok.Getter;

@Getter
public class FdDto {
    int amount;
    FdCompoundingFrequency fdCompoundingFrequency;
    int tenure;
}

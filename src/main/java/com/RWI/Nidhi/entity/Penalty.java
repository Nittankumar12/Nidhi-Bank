package com.RWI.Nidhi.entity;

import com.RWI.Nidhi.enums.PenaltyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Penalty {
    @Id
    private int penaltyId;//Unique Id
    private Double penaltyAmount;// total penalty amount
    private LocalDate dueDate; // next date on which penalty is to be paid
    @Enumerated(EnumType.STRING)
    private PenaltyStatus penaltyStatus;//status
    @ManyToOne
    @JoinColumn
    private Loan loan;
}
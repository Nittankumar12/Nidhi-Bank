package com.RWI.Nidhi.entity;

import com.RWI.Nidhi.enums.PenaltyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Penalty {
    @Id
    private int penaltyId;
    @OneToOne
    private Loan loanId;
    private Date pendingPenaltyMonths;
    private Date numberOfMonthsEmiDueFor;
    private double totalEmiWithFine;
    @Enumerated(EnumType.STRING)
    private PenaltyStatus penaltyStatus;
    @ManyToOne
    @JoinColumn
    private Loan loan;
}

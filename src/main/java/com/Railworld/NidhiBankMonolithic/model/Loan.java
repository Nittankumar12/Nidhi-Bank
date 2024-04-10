package com.Railworld.NidhiBankMonolithic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String loanType;
    private Double amount;
    private LoanStatus loanStatus;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name= "account_id")
    private Account account;

}

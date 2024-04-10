package com.Railworld.NidhiBankMonolithic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Long accountNo;
    private Integer pin;
    private String accountType;
    private String IFSC_code;

    private String branch;
    private Double balance;

    @OneToOne(cascade =  CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(cascade =  CascadeType.ALL, mappedBy = "from")
    private List<Transaction> transactionsFrom;

    @OneToMany(cascade =  CascadeType.ALL, mappedBy = "to")
    private List<Transaction> transactionsTo;

    @OneToOne(cascade = CascadeType.ALL,mappedBy = "account")
    private Loan loan;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "account")
    private List<Application> applications;
}

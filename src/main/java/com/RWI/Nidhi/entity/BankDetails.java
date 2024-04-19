package com.RWI.Nidhi.entity;

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
public class BankDetails {
    @Id
    private int id;
    private String accHolderName;
    private long accNumber;
    private String bankName;
    private String bankBranch;
    private String IFSCCode;
    @OneToOne
    @JoinColumn
    private User user;
}

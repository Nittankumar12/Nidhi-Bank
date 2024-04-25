package com.RWI.Nidhi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    private int userId;
    private String userName;
    private String phoneNumber;
    private String email;
    private String password;
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Accounts accounts;
    @ManyToOne
    @JoinColumn
    private Agent agent;
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private BankDetails bankDetails;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private KYC kyc;
}

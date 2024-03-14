package com.Railworld.NidhiBank.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Member {
    @Id
    @GeneratedValue
    private Integer mId;
    private String mName;
    private String mEmail;
    private String mPassword;
    private Integer mAccountNo;
    private Integer mPin;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cId")
    private Company company;

    @OneToOne(mappedBy = "member")
    private Loan loan;

    @OneToMany(mappedBy = "member")
    private List<Transaction> transactionList;


    @ElementCollection(targetClass = Role.class)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private List<Role> roles;
}

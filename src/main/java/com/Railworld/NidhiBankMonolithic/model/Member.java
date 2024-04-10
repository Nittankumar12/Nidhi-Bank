package com.Railworld.NidhiBankMonolithic.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Integer mId;
    private String mName;
    private String mEmail;
    private String mRegistrationDate;
    private String address;
    private String phoneNo;

    @OneToOne(cascade = CascadeType.ALL,mappedBy = "member")
    @JoinColumn(name = "account")
    private Account account;

    @ManyToOne(cascade =  CascadeType.ALL)
    @JoinColumn(name = "c_id")
    private Company company;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "member")
    @JoinColumn(name = "user_id")
    private User user;


}

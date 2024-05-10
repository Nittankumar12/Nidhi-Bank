package com.RWI.Nidhi.entity;

import com.RWI.Nidhi.Security.models.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userId;
    private String userName;
    private String phoneNumber;
    private String email;
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(  name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id")
            ,inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Accounts accounts;

    @ManyToOne
    @JoinColumn
    private Agent agent;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private BankDetails bankDetails;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private KYC kyc;

    public User(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }
}

package com.RWI.Nidhi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int agentId;
    private String agentName;
    private String agentPhoneNum;
    private String agentAddress;
    private String agentEmail;
    private String agentPassword;
    @OneToMany(mappedBy = "agent",cascade = CascadeType.ALL)
    private List<User> userList;
    @OneToMany(mappedBy = "agent",cascade = CascadeType.ALL)
    private List<FixedDeposit> fixedDepositList;
    @OneToMany(mappedBy = "agent",cascade = CascadeType.ALL)
    private List<RecurringDeposit> recurringDepositList;
    @OneToMany(mappedBy = "agent",cascade = CascadeType.ALL)
    private List<MIS> misList;
    @OneToMany(mappedBy = "agent",cascade = CascadeType.ALL)
    private List<Scheme> schemeList;
}

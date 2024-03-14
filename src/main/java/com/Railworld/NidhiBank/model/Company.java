package com.Railworld.NidhiBank.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Data
public class Company {
    @Id
    @GeneratedValue
    private Integer cId;
    private String cName;
    private String cEmail;
    private String cAddress;
    private Integer cBalance;

    @OneToMany(mappedBy = "company",cascade = CascadeType.ALL)
    private List<Member> memberList;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<User> userList;
}

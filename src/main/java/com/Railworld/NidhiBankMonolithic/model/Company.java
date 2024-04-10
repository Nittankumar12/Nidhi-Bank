package com.Railworld.NidhiBankMonolithic.model;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.List;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cId;
    private String cName;
    private String cEmail;
    private String cAddress;
    private Double cBalance;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "company")
    private List<Member> members;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "company")
    private List<User> users;

}

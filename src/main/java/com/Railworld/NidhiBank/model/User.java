package com.Railworld.NidhiBank.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue
    private Integer uId;
    private String uEmail;
    private String uName;
    private String uPassword;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cId")
    private Company company;

}

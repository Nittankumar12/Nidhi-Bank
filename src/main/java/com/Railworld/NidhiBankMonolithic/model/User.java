package com.Railworld.NidhiBankMonolithic.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name =  "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uId;
    private String uEmail;
    private String uPassword;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "c_id")
    private Company company;

    private boolean joined;

    @OneToOne(cascade = CascadeType.ALL)
    private Member member;
}

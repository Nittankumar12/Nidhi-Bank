package com.Railworld.NidhiBank.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
public class Loan {
    @Id
    @GeneratedValue
    private Integer lId;
    private String lType;
    private Integer lAmount;
    private String lStatus;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mId")
    private Member member;
}

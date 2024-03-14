package com.Railworld.NidhiBank.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
public class Transaction {
    @Id
    @GeneratedValue
    private Integer tId;
    private Integer tAmount;
    private String tType;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mId")
    private Member member;

}

package com.Railworld.NidhiBankMonolithic.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private TransactionType type;
    private Double amount;
    private Date date;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "from_account_id")
    private Account from;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "to_account_id")
    private Account to;
}

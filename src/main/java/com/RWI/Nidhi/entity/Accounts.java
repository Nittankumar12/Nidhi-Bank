package com.RWI.Nidhi.entity;

import com.RWI.Nidhi.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Accounts {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int accountId;
	@Column(nullable = false, length = 14)
	private String accountNumber;
	@Column(nullable = false)
	@Value("${some.key:50}")
	private double currentBalance;
	@CreatedDate
	private LocalDate accountOpeningDate;
	@Enumerated(EnumType.STRING)
	private Status accountStatus;
	private String Pin;
	@OneToOne
	@JoinColumn
	private User user;
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	private List<FixedDeposit> fdList;
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	private List<Loan> loanList;
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	private List<MIS> misList;
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	private List<RecurringDeposit> recurringDepositList;
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	private List<Scheme> schemeList;
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	private List<Transactions> transactionsList;
}

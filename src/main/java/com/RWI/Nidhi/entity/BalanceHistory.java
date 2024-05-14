package com.RWI.Nidhi.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BalanceHistory {
    @Id
    private int id;
    private LocalDate date;
    private double balance;

//	public BalanceHistory() {
//		super();
//		//
//	}
//	public BalanceHistory(int id, LocalDate date, double balance) {
//		super();
//		this.id = id;
//		this.date = date;
//		this.balance = balance;
//	}
//	public int getId() {
//		return id;
//	}
//	public void setId(int id) {
//		this.id = id;
//	}
//	public LocalDate getDate() {
//		return date;
//	}
//	public void setDate(LocalDate date) {
//		this.date = date;
//	}
//	public double getBalance() {
//		return balance;
//	}
//	public void setBalance(double balance) {
//		this.balance = balance;
//	}
//
    
}


package com.RWI.Nidhi.repository;

import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.entity.Scheme;
import com.RWI.Nidhi.enums.Status;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountsRepo extends JpaRepository<Accounts, Integer> {

	double findCurrentBalanceByAccountId(int accountId);

	Optional<Accounts> findByAccountNumber(String accountNumber);
}

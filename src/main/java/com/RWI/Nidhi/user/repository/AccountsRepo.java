package com.RWI.Nidhi.user.repository;

import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.entity.Scheme;
import com.RWI.Nidhi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountsRepo extends JpaRepository<Accounts, Integer> {
    double findCurrentBalanceByAccountId(int accountId);
    List<Scheme> findSchemeListByAccountId(int accountId);
    Optional<Accounts> findByAccountNumber(String accountNumber);

    String findAccountNumberByAccountId(int accountId);
    User findUserByAccountId(int accountId);

}

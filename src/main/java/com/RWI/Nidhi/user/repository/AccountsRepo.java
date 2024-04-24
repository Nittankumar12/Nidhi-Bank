package com.RWI.Nidhi.user.repository;

import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.entity.Scheme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountsRepo extends JpaRepository<Accounts, Integer> {
    double findCurrentBalanceByAccountId(int accountId);
    List<Scheme> findSchemeListByAccountId(int accountId);
}

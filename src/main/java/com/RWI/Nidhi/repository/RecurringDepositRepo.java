package com.RWI.Nidhi.repository;

import com.RWI.Nidhi.entity.RecurringDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecurringDepositRepo extends JpaRepository<RecurringDeposit,Integer> {
}

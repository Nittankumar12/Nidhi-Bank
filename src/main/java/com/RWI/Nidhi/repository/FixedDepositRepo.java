package com.RWI.Nidhi.repository;

import com.RWI.Nidhi.entity.FixedDeposit;
import com.RWI.Nidhi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FixedDepositRepo extends JpaRepository<FixedDeposit, Integer> {
}

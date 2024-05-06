package com.RWI.Nidhi.repository;

import com.RWI.Nidhi.entity.FixedDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FixedDepositRepo extends JpaRepository<FixedDeposit, Integer> {

}

package com.RWI.Nidhi.repository;

import com.RWI.Nidhi.entity.Transactions;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepo extends JpaRepository<Transactions, Integer> {
    @Transactional
    @Modifying
    @Query(value = "select * from transactions where transaction_date between :startDate and :endDate", nativeQuery = true)
    List<Transactions> getTransactionBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}

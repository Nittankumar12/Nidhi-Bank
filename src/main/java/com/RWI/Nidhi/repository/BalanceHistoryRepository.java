package com.RWI.Nidhi.repository;

import com.RWI.Nidhi.entity.BalanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BalanceHistoryRepository extends JpaRepository<BalanceHistory, Long> {

    List<BalanceHistory> findByDateBetween( LocalDate startDate, LocalDate endDate);

    List<BalanceHistory> findByDate(LocalDate today);
}



package com.RWI.Nidhi.admin.adminServiceImplementation;


import com.RWI.Nidhi.entity.BalanceHistory;
import com.RWI.Nidhi.repository.BalanceHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BalanceHistoryService {

    @Autowired
    private BalanceHistoryRepository balanceHistoryRepository;

    public List<BalanceHistory> getDailyBalanceHistory() {
        LocalDate today = LocalDate.now();
        return balanceHistoryRepository.findByDate(today);
    }

    public List<BalanceHistory> getWeeklyBalanceHistory() {
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate endOfWeek = now.with(java.time.temporal.TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
        return balanceHistoryRepository.findByDateBetween(startOfWeek, endOfWeek);
    }

    public List<BalanceHistory> getMonthlyBalanceHistory() {
        LocalDate startOfMonth = LocalDate.now().with(java.time.temporal.TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = LocalDate.now().with(java.time.temporal.TemporalAdjusters.lastDayOfMonth());
        return balanceHistoryRepository.findByDateBetween(startOfMonth, endOfMonth);
    }
}

package com.RWI.Nidhi.admin.controller;

import com.RWI.Nidhi.entity.BalanceHistory;
import com.RWI.Nidhi.admin.adminServiceImplementation.BalanceHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/balance-history")
public class BalanceHistoryController {

    @Autowired
    private BalanceHistoryService balanceHistoryService;

    @GetMapping("/daily/{userId}")
    public List<BalanceHistory> getDailyBalanceHistory() {
        return balanceHistoryService.getDailyBalanceHistory();
    }

    @GetMapping("/weekly/{userId}")
    public List<BalanceHistory> getWeeklyBalanceHistory() {
        return balanceHistoryService.getWeeklyBalanceHistory();
    }

    @GetMapping("/monthly/{userId}")
    public List<BalanceHistory> getMonthlyBalanceHistory() {
        return balanceHistoryService.getMonthlyBalanceHistory();
    }
}


package com.RWI.Nidhi.notification;

import com.RWI.Nidhi.entity.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loans")
public class LoanRemindController {
    @Autowired
    private LoanNotificationService loanNotificationService;

    @PostMapping("/sendNotifications")
    public ResponseEntity<String> sendNotifications() {
        loanNotificationService.sendLoanReminders();
        return ResponseEntity.ok("Loan notifications sent successfully.");
    }

}

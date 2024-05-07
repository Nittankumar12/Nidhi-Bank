package com.RWI.Nidhi.notification;

import com.RWI.Nidhi.entity.Loan;
import com.RWI.Nidhi.repository.LoanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanNotificationService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private LoanRepo loanRepository;

    @Scheduled(cron = "0 0 0 * * *") // Runs daily at midnight
    public void sendLoanReminders() {
        LocalDate currentDate = LocalDate.now();
        LocalDate emiDateThreshold = currentDate.plusDays(5);
        List<Loan> loansDueSoon = loanRepository.findByEmiDateBetween(currentDate, emiDateThreshold);

        for (Loan loan : loansDueSoon) {
            String borrowerEmail = loan.getUser().getEmail();
            String subject = "Loan Reminder";
            String message = " Dear borrower, your loan is due in 5 days. Please ensure timely repayment.";

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(borrowerEmail);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);

            javaMailSender.send(mailMessage);
        }
    }
}


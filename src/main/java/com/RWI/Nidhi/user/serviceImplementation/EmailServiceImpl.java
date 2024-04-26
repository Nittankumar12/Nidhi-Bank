package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.user.serviceInterface.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {
    private Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    @Autowired
    private JavaMailSender mailSender;


    @Override
    public void sendEmail(List<String> toList, String subject, String message, List<String> ccList, List<String> bccList, List<MultipartFile> files) {
        for (String to : toList) {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setFrom("piyush307hit@gmail.com");
                helper.setTo(to);
                helper.setText(message);
                helper.setSubject(subject);
                if (ccList != null && !ccList.isEmpty()) {
                    for (String cc : ccList) {
                        helper.addCc(cc);
                    }
                }
                if (bccList != null && !bccList.isEmpty()) {
                    for (String bcc : bccList) {
                        helper.addBcc(bcc);
                    }
                }

                for (MultipartFile file : files) {
                    // Attach each file to the email
                    ByteArrayResource resource = new ByteArrayResource(file.getBytes());
                    helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), resource);
                }

                // Send the email
                mailSender.send(mimeMessage);
                log.info("Email sent successfully");
            } catch (MessagingException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


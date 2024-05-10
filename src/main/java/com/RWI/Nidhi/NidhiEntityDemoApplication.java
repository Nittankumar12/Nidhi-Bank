package com.RWI.Nidhi;

import com.RWI.Nidhi.configuration.TwilioConfig;
import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NidhiEntityDemoApplication {

    @Autowired
    private TwilioConfig twilioConfig;


    public static void main(String[] args) {
        SpringApplication.run(NidhiEntityDemoApplication.class, args);
    }

    @PostConstruct
    public void setup() {
        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
    }
}

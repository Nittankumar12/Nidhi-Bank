package com.RWI.Nidhi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.twilio.Twilio;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class NidhiEntityDemoApplication {

	@Autowired
	private com.RWI.Nidhi.user.configuration.TwilioConfig twilioConfig;

	@PostConstruct
	public void setup() {
		Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
	}

	public static void main(String[] args) {
		SpringApplication.run(NidhiEntityDemoApplication.class, args);
	}

}
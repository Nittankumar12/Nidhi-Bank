package com.RWI.Nidhi.otpSendAndVerify;

import com.RWI.Nidhi.configuration.TwilioConfig;
import com.RWI.Nidhi.exception.OTPExpireException;
import com.RWI.Nidhi.exception.OtpNotSendException;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OtpServiceImplementation implements OtpServiceInterface {

	@Autowired
	private TwilioConfig twilioConfig; // Prince twilio config

	@Autowired
	private JavaMailSender javaMailSender;

	// Expiry time for OTP in milliseconds
	private static final long OTP_EXPIRY_TIME_MILLIS = 2 * 60 * 1000;

	// in scope of Mr Piyush and Mr Prince
	private String otp;

	private long otpGenerationTimeMillis; // Timestamp for OTP generation

	// implemented by Mr Piyush
	@Override
	public ResponseEntity<String> sendEmailOtp(String userEmailId,String subject, String messageToSend, String otp) throws Exception {
		this.otp = otp;
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("nitin01kumar14@gmail.com");
		message.setTo(userEmailId);
		message.setSubject(subject);
		message.setText(messageToSend + this.otp);
		javaMailSender.send(message);

		return ResponseEntity.ok("Message sent to " + userEmailId);
	}

	// implemented by Mr Piyush
	@Override
	public ResponseEntity<String> verifyEmailOtp(String userEmailId, String enteredOTP) throws Exception {
		if (otp.equals(enteredOTP)) {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(userEmailId);
			message.setText("your email verification is done");
			javaMailSender.send(message);
			return ResponseEntity.ok("Email " + userEmailId + " verified successfully!");
		} else {
			// Incorrect OTP
			return ResponseEntity.ok("Incorrect OTP. Email verification failed.");
		}
	}

	// implemented by Mr Prince
	@Override
	public ResponseEntity<String> sendPhoneOtp(String phoneNumber) throws Exception {

		try {
			// Initialize Twilio
			Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());

			// Adding country code +91 to the phone number
			phoneNumber = "+91" + phoneNumber;

			// Create a Twilio phone number object for recipient and sender
			PhoneNumber to = new PhoneNumber(phoneNumber);
			PhoneNumber from = new PhoneNumber(twilioConfig.getPhoneNumber());

			System.out.println(to);

			// Generate OTP
			otp = generateOTP();
			otpGenerationTimeMillis = System.currentTimeMillis();

			// OTP message
			String otpMessage = "Dear Customer, your OTP is " + otp
					+ " for sending SMS through Nidhi Bank application. Thank you.";

			// Send SMS using Twilio
			Message.creator(to, from, otpMessage).create();

//			return ResponseEntity.ok("OTP sent successfully.");
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Failed to send OTP.");
//		}
//	}
			// Check if OTP was sent successfully
			if (otp == null || otp.isEmpty()) {
				throw new OtpNotSendException("Failed to send OTP."); // Throw OtpNotSendException
			}

			return ResponseEntity.ok("OTP sent successfully." + otp);
		} catch (OtpNotSendException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Failed to send OTP.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Failed to send OTP.");
		}
	}

	public String generateOTP() {
		// Generate a 6-digit OTP
		return String.format("%06d", new Random().nextInt(999999));
	}

	// implemented by Mr Prince
	@Override
	public ResponseEntity<String> verifyPhoneOtp(String phoneNumber, String enteredOtp) throws Exception {
		try {
			// Adding country code +91 to the phone number
			phoneNumber = "+91" + phoneNumber;

//			// Check if OTP has expired
//			long currentTimeMillis = System.currentTimeMillis();
//			if ((currentTimeMillis - otpGenerationTimeMillis) > OTP_EXPIRY_TIME_MILLIS) {
//				return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("OTP has been expired!");
//			}

			// Check if OTP has expired
			long currentTimeMillis = System.currentTimeMillis();
			if ((currentTimeMillis - otpGenerationTimeMillis) > OTP_EXPIRY_TIME_MILLIS) {
				throw new OTPExpireException("OTP has been expired!"); // Throw OTPExpireException
			}

			// Compare the OTP entered by the user with the OTP sent to the phone number
			if (otp != null && otp.equals(enteredOtp)) {
				// OTP is valid
				return ResponseEntity.ok("OTP is valid!");
			} else {
				// OTP is invalid
				return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Entered OTP is invalid!");
			}
		} catch (OTPExpireException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("OTP has been expired!");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Failed to verify OTP.");

		}

	}

}

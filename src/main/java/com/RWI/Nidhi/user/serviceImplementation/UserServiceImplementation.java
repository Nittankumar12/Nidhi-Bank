package com.RWI.Nidhi.user.serviceImplementation;

import java.util.Random;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.RWI.Nidhi.exception.OTPExpireException;
import com.RWI.Nidhi.exception.OtpNotSendException;
import com.RWI.Nidhi.user.configuration.TwilioConfig;
import com.RWI.Nidhi.user.serviceInterface.UserServiceInterface;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class UserServiceImplementation implements UserServiceInterface {

	@Autowired
	private TwilioConfig twilioConfig; // Prince twilio config

	// Expiry time for OTP in milliseconds
	private static final long OTP_EXPIRY_TIME_MILLIS = 2 * 60 * 1000;

	// in scope of Mr Piyush and Mr Prince
	private String sentOtp;

	private long otpGenerationTimeMillis; // Timestamp for OTP generation

	// implemented by Mr Piyush
	@Override
	public ResponseEntity<String> sendEmailOtp(String email) throws Exception {
		return null;
	}

	// implemented by Mr Piyush
	@Override
	public ResponseEntity<String> verifyEmailOtp(String email, String sentOtp, String enteredOtp) throws Exception {
		return null;
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
			sentOtp = generateOTP();
			otpGenerationTimeMillis = System.currentTimeMillis();

			// OTP message
			String otpMessage = "Dear Customer, your OTP is " + sentOtp
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
			if (sentOtp == null || sentOtp.isEmpty()) {
				throw new OtpNotSendException("Failed to send OTP."); // Throw OtpNotSendException
			}

			return ResponseEntity.ok("OTP sent successfully." + sentOtp);
		} catch (OtpNotSendException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Failed to send OTP.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Failed to send OTP.");
		}
	}

	private String generateOTP() {
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
			if (sentOtp != null && sentOtp.equals(enteredOtp)) {
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

package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.enums.Status;
import com.RWI.Nidhi.repository.UserRepo;
import com.RWI.Nidhi.user.serviceInterface.UserService;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepo userRepo;

	@Override
	public User getByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	@Override
	public boolean authenticate(String email, String password) {

		Optional<User> user = userRepo.findUserByEmail(email);
		if (user != null && user.get().getAccounts().getAccountStatus().equals("ACTIVE")) {
			return (user.get().getPassword().equals(getEncryptedPassword(password)))
					&& email.equals(user.get().getEmail());
		} else {
			return false;
		}
	}

	private String getEncryptedPassword(String password) {
		// String encryptedPassword = "";
		try {
			BigInteger number = new BigInteger(1, getSHA(password));
			StringBuilder hexString = new StringBuilder(number.toString(16));
			return hexString.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private byte[] getSHA(String input) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			return messageDigest.digest(input.getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

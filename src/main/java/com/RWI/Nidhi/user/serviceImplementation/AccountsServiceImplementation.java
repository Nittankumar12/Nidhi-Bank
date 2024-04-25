package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.entity.Scheme;
import com.RWI.Nidhi.enums.Status;
import com.RWI.Nidhi.exception.AccountIdNotFoundException;
import com.RWI.Nidhi.exception.AccountNotFoundException;
import com.RWI.Nidhi.user.repository.AccountsRepo;
import com.RWI.Nidhi.user.serviceInterface.AccountsService;
import com.RWI.Nidhi.user.serviceInterface.SchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AccountsServiceImplementation implements AccountsService {
    @Autowired
    AccountsRepo accountsRepo;
    @Autowired
    SchemeService schemeService;

	// Define the length of the account number
	private static final int ACCOUNT_NUMBER_LENGTH = 5;


	@Override
	public Boolean schemeRunning(int accountId) {
		List<Scheme> currentScheme = accountsRepo.findSchemeListByAccountId(accountId);
		for (Scheme sc : currentScheme) {
			int remainingDays = schemeService.findSchemeRemainingDays(sc.getSchemeId());
			if (remainingDays != 0)
				return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
    
    //Prince code
    


 	// Generate a unique account number
 	private String generateRandomAccountNumber() {
 		StringBuilder sb = new StringBuilder();

 		// Append fixed prefix
 		sb.append("1374104");

 		// Add random digits to the account number
 		Random random = new Random();
 		for (int i = 0; i < ACCOUNT_NUMBER_LENGTH; i++) {
 			sb.append(random.nextInt(10)); // Generate random digits (0-9)
 		}
 		return sb.toString();
 	}

 	@Override
 	public Accounts openAccount() {
 		// Generate a random account number
 		String accountNumber = generateRandomAccountNumber();

 		// Create a new account object
 		Accounts newAccount = new Accounts();
 		newAccount.setAccountNumber(accountNumber);
 		newAccount.setCurrentBalance(0); // Set the initial balance to 0
 		newAccount.setAccountStatus(Status.ACTIVE);
 		return accountsRepo.save(newAccount);
 	}

 	@Override
 	public Status getAccountStatus(int accountId) {
 		try {
 			// Retrieve account from the database
 			Optional<Accounts> optionalAccount = accountsRepo.findById(accountId);

 			// Check if the account exists
 			if (optionalAccount.isPresent()) {
 				Accounts account = optionalAccount.get();
 				return account.getAccountStatus(); // Return the account status
 			} else {
 				throw new AccountIdNotFoundException("Account with ID " + accountId + " not found");
 			}
 		} catch (Exception ex) {
 			ex.printStackTrace();
 			throw new AccountIdNotFoundException("Error occurred while fetching account status");
 		}
 	}

 	@Override
 	public double checkAccountBalanceByNumber(String accountNumber) {
 		try {
 			// Retrieve account from the database by account number
 			Optional<Accounts> optionalAccount = accountsRepo.findByAccountNumber(accountNumber);

 			// Check if the account exists
 			if (optionalAccount.isPresent()) {
 				Accounts account = optionalAccount.get();
 				return account.getCurrentBalance(); // Return the account balance
 			} else {
 				throw new AccountNotFoundException("Account with number " + accountNumber + " not found");
 			}
 		} catch (Exception ex) {
 			ex.printStackTrace();
 			throw new AccountNotFoundException("Error occurred while fetching account balance");
 		}
 	}
}

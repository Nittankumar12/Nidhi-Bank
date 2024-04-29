package com.RWI.Nidhi.user.serviceImplementation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.entity.Scheme;
import com.RWI.Nidhi.enums.Status;
import com.RWI.Nidhi.exception.AccountIdNotFoundException;
import com.RWI.Nidhi.exception.AccountNotFoundException;
import com.RWI.Nidhi.user.repository.AccountsRepo;
import com.RWI.Nidhi.user.serviceInterface.AccountsServiceInterface;
import com.RWI.Nidhi.user.serviceInterface.SchemeServiceInterface;

@Service
public class AccountsServiceImplementation implements AccountsServiceInterface {
	@Autowired
	AccountsRepo accountsRepo;
	@Autowired
	SchemeServiceInterface schemeServiceInterface;

	// Define the length of the account number
	private static final int ACCOUNT_NUMBER_LENGTH = 8;

	String accountPIN = generateRandomAccountPIN();

	@Override
	public Boolean schemeRunning(int accountId) {
		List<Scheme> currentScheme = accountsRepo.findSchemeListByAccountId(accountId);
		for (Scheme sc : currentScheme) {
			int remainingDays = schemeServiceInterface.findSchemeRemainingDays(sc.getSchemeId());
			if (remainingDays != 0)
				return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	// Prince code
	// Generate a unique account number
	private String generateRandomAccountNumber() {
		StringBuilder sb = new StringBuilder();

		// Append fixed prefix
		sb.append("1374");

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
		newAccount.setAccountOpeningDate(LocalDate.now());
		newAccount.setAccountStatus(Status.ACTIVE);
		newAccount.setPin(accountPIN); // Set the account PIN
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

	// Generate a unique account PIN
	public String generateRandomAccountPIN() {
		final int PIN_LENGTH = 4;
		StringBuilder pin = new StringBuilder();

		// Add random digits to the PIN
		Random random = new Random();
		for (int i = 0; i < PIN_LENGTH; i++) {
			pin.append(random.nextInt(4)); // Generate random digits (0-9)
		}
		return pin.toString();
	}

	@Override
	public void updateAccountPIN(String accountNumber, String newPIN) {
		try {
			// Retrieve account from the database
			Optional<Accounts> Account = accountsRepo.findByAccountNumber(accountNumber);

			// Check if the account exists
			if (Account.isPresent()) {
				Accounts account = Account.get();

				// Update the account PIN
				account.setPin(newPIN);

				// Save the updated account
				accountsRepo.save(account);
			} else {
				throw new AccountIdNotFoundException("Account with account number " + accountNumber + " not found");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AccountIdNotFoundException("Error occurred while updating account PIN");
		}

	}

	@Override
	public void fundTransfer(String sourceAccountNumber, String destinationAccountNumber, double amount) {

		try {
			// Retrieve source account from the database
			Optional<Accounts> optionalSourceAccount = accountsRepo.findByAccountNumber(sourceAccountNumber);
			// Retrieve destination account from the database
			Optional<Accounts> optionalDestinationAccount = accountsRepo.findByAccountNumber(destinationAccountNumber);
			System.out.println("HIi");

			// Check if both accounts exist
			if (optionalSourceAccount.isPresent() && optionalDestinationAccount.isPresent()) {
				Accounts sourceAccount = optionalSourceAccount.get();
				Accounts destinationAccount = optionalDestinationAccount.get();

				// Check if source account has sufficient balance
				if (sourceAccount.getCurrentBalance() >= amount) {
					// Deduct the amount from the source account
					sourceAccount.setCurrentBalance(sourceAccount.getCurrentBalance() - amount);
					// Add the amount to the destination account
					destinationAccount.setCurrentBalance(destinationAccount.getCurrentBalance() + amount);

					// Save updated accounts to the database
					accountsRepo.save(sourceAccount);
					accountsRepo.save(destinationAccount);
				} else {
					throw new IllegalArgumentException("Insufficient balance in the source account");
				}
			} else {
				throw new AccountNotFoundException("One of the accounts involved in the transfer was not found");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Error occurred during fund transfer");
		}

	}

}

package com.RWI.Nidhi.user.serviceImplementation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import com.RWI.Nidhi.entity.*;
import com.RWI.Nidhi.enums.CommissionType;
import com.RWI.Nidhi.enums.KycStatus;
import com.RWI.Nidhi.repository.*;
import com.RWI.Nidhi.user.serviceInterface.UserService;
import com.twilio.rest.api.v2010.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.RWI.Nidhi.dto.AccountResponseDTO;
import com.RWI.Nidhi.dto.BankRequestDTO;

import com.RWI.Nidhi.enums.Status;
import com.RWI.Nidhi.exception.AccountIdNotFoundException;
import com.RWI.Nidhi.exception.AccountNotFoundException;
import com.RWI.Nidhi.user.serviceInterface.AccountsServiceInterface;

@Service
public class AccountsServiceImplementation implements AccountsServiceInterface {

	@Autowired
	private AccountsRepo accountsRepo;
	@Autowired
	CommissionRepository commissionRepo;
	@Autowired
	AgentRepo agentRepo;
	@Autowired
	KycDetailsRepo kycDetailsRepo;
	@Autowired
	private BankRepo bankRepo;
	@Autowired
	UserService userService;
	@Autowired
	private UserRepo userRepo;

	String accountPIN = generateRandomAccountPIN();

	private String generateAccountNumber(String name, String mobileNumber) {
//		 Extract the first two letters of the name and convert them to uppercase
//	     String initials = name.substring(0, Math.min(name.length(), 1)).toUpperCase();
		char firstChar = name.charAt(0);
		char lastChar = name.charAt(name.lastIndexOf(' ') + 1);

		// Extract the last eight digits of the mobile number
		// String lastDigits = mobileNumber.substring(Math.max(0, mobileNumber.length()
		// - 8));

		// Concatenate the initials and last digits to form the account number
		return (firstChar + mobileNumber + lastChar).toUpperCase();
	}

	@Override
	public ResponseEntity<?> openAccount(String email) {
		KycDetails kycDetails = kycDetailsRepo.findByEmail(email);
		if(kycDetails == null) return new ResponseEntity<>("Kyc Details for this not found", HttpStatus.NOT_FOUND);
		if(!kycDetails.getKycStatus().equals(KycStatus.Approved)) return new ResponseEntity<>("Kyc for current user is not approved yet" , HttpStatus.NOT_ACCEPTABLE);

		// Find the user by email
		User optionalUser = userRepo.findByEmail(email);
		if (optionalUser != null) {
			// String accountNumber = generateRandomAccountNumber();
			String accountNumber = generateAccountNumber(optionalUser.getUserName(), optionalUser.getPhoneNumber());

			// Create a new account object
			Accounts newAccount = new Accounts();
			newAccount.setAccountNumber(accountNumber);
			newAccount.setCurrentBalance(0); // Set the initial balance to 0
			newAccount.setAccountOpeningDate(LocalDate.now());
			newAccount.setAccountStatus(Status.ACTIVE);
			newAccount.setPin(generateRandomAccountPIN()); // Set the account PIN
			// Associate the account with the user
			newAccount.setTransactionsList(new ArrayList<>());
			User user = optionalUser;
			newAccount.setUser(user);

			//Commission
			Commission commission = new Commission();
			commission.setAgent(user.getAgent());
			commission.setUser(user);
			commission.setCommissionType((CommissionType.AccountOpen));
			commission.setCommissionRate(0);
			commission.setCommDate(LocalDate.now());
			commission.setCommissionAmount(500.0);
			commissionRepo.save(commission);
			userRepo.save(user);
			agentRepo.save(user.getAgent());
			accountsRepo.save(newAccount);

			// Create and return AccountResponseDTO
			return new ResponseEntity<>(createAccountResponseDTO(newAccount),HttpStatus.OK);
		} else {
			// Handle the case where the user is not found
			throw new RuntimeException("User with email " + email + " not found");
		}
	}
	@Override
	public double amountCalc(double commissionRate, double amount){
		double v = amount * commissionRate / 100;
		return v;
	}
	@Override
	public ResponseEntity<?> checkAccount(String email){
		User user = userService.getByEmail(email);
		if(user==null)return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
		Accounts account = user.getAccounts();
		if(account==null)return new ResponseEntity<>("Account doesn't exist", org.springframework.http.HttpStatus.BAD_REQUEST);
		Status status = account.getAccountStatus();
		if(status.equals(Account.Status.CLOSED)||status.equals(Account.Status.SUSPENDED))return  new ResponseEntity<>("Account ", HttpStatus.BAD_REQUEST);
		else return new ResponseEntity<>(account.getAccountStatus(), HttpStatus.OK);
	}

	private AccountResponseDTO createAccountResponseDTO(Accounts account) {
		AccountResponseDTO dto = new AccountResponseDTO();
		dto.setAccountId(account.getAccountId());
		dto.setAccountNumber(account.getAccountNumber());
		dto.setStatus(account.getAccountStatus());
		dto.setUserName(account.getUser().getUserName());
		dto.setUserEmail(account.getUser().getEmail());
		return dto;
	}
	@Override
	public Boolean CheckAccStatus(String userEmail){
		User user = userRepo.findByEmail(userEmail);
		Accounts accounts = user.getAccounts();
		if(!accounts.getAccountStatus().equals(Status.ACTIVE))
			return Boolean.FALSE;
		return Boolean.TRUE;
	}

	@Override
	public Status checkAccountStatus(String accountNumber) {
		try {
			// Retrieve account from the database
			Optional<Accounts> optionalAccount = accountsRepo.findByAccountNumber(accountNumber);

			// Check if the account exists
			if (optionalAccount.isPresent()) {
				Accounts account = optionalAccount.get();
				return account.getAccountStatus(); // Return the account status
			} else {
				throw new AccountIdNotFoundException("Account with ID " + accountNumber + " not found");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AccountIdNotFoundException("Error occurred while fetching account status");
		}
	}

	@Override
	public Status checkAccountStatus(int accountId) {
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

	@Override
	public void addBankUserDetails(BankRequestDTO bankDto, String emailId) {
		User userOptional = userRepo.findByEmail(emailId);
		if (userOptional != null) {
			User user = userOptional;

			BankDetails bankDetails = new BankDetails();

			// bank details from the DTO
			bankDetails.setAccHolderName(user.getUserName());
			bankDetails.setAccNumber(bankDto.getAccountNumber());
			bankDetails.setBankBranch(bankDto.getBranchName());
			bankDetails.setBankName(bankDto.getBankName());
			bankDetails.setIFSCCode(bankDto.getIfsc());

			// bank details with the user
			user.setBankDetails(bankDetails);

			// Save the bank details
			userRepo.save(user);

			// bankDetails.setUser(user);
			// bankRepo.save(bankDetails);
		} else {
			// When the user is not found
			System.out.println("User with email " + emailId + " not found");
		}
	}

	@Override
	public void addBalance(String accountNumber, double amount) {
		// TODO Auto-generated method stub
		try {
			// Retrieve account from the database
			Optional<Accounts> optionalAccount = accountsRepo.findByAccountNumber(accountNumber);

			// Check if the account exists
			if (optionalAccount.isPresent()) {
				Accounts account = optionalAccount.get();

				// Add the amount to the account balance
				double currentBalance = account.getCurrentBalance();
				account.setCurrentBalance(currentBalance + amount);

				// Save the updated account
				accountsRepo.save(account);
			} else {
				throw new AccountIdNotFoundException("Account with account number " + accountNumber + " not found");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AccountIdNotFoundException("Error occurred while adding balance to the account");
		}
	}
}

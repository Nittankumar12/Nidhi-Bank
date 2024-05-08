package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.dto.BankDetailsDTO;
import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.entity.BankDetails;
import com.RWI.Nidhi.entity.Scheme;
import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.enums.Status;
import com.RWI.Nidhi.exception.AccountIdNotFoundException;
import com.RWI.Nidhi.exception.AccountNotFoundException;
import com.RWI.Nidhi.repository.AccountsRepo;
import com.RWI.Nidhi.repository.BankRepo;
import com.RWI.Nidhi.repository.UserRepo;
import com.RWI.Nidhi.user.serviceInterface.AccountsServiceInterface;
import com.RWI.Nidhi.user.serviceInterface.SchemeServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AccountsServiceImplementation implements AccountsServiceInterface {
    String accountPIN = generateRandomAccountPIN();
    // Define the length of the account number
    // private static final int ACCOUNT_NUMBER_LENGTH = 10;
    @Autowired
    private AccountsRepo accountsRepo;
    @Autowired
    private SchemeServiceInterface schemeServiceInterface;
    @Autowired
    private BankRepo bankRepo;
    @Autowired
    private UserRepo userRepo;


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

    private String generateAccountNumber(String name, String mobileNumber) {
        // Extract the first two letters of the name and convert them to uppercase
//	    String initials = name.substring(0, Math.min(name.length(), 1)).toUpperCase();
        char firstChar = name.charAt(0);
        char lastChar = name.charAt(name.lastIndexOf(' ') + 1);

        // Extract the last eight digits of the mobile number
        // String lastDigits = mobileNumber.substring(Math.max(0, mobileNumber.length()
        // - 8));

        // Concatenate the initials and last digits to form the account number
        return (firstChar + mobileNumber + lastChar).toUpperCase();
    }

    @Override
    public Accounts openAccount(String email) {
        // Generate a random account number

        // Find the user by email
        Optional<User> optionalUser = userRepo.findUserByEmail(email);
        if (optionalUser.isPresent()) {
            // String accountNumber = generateRandomAccountNumber();
            String accountNumber = generateAccountNumber(optionalUser.get().getUserName(),
                    optionalUser.get().getPhoneNumber());


            // Create a new account object
            Accounts newAccount = new Accounts();
            newAccount.setAccountNumber(accountNumber);
            newAccount.setCurrentBalance(0); // Set the initial balance to 0
            newAccount.setAccountOpeningDate(LocalDate.now());
            newAccount.setAccountStatus(Status.ACTIVE);
            newAccount.setPin(generateRandomAccountPIN()); // Set the account PIN
            // Associate the account with the user
            User user = optionalUser.get();
            newAccount.setUser(user);
            return accountsRepo.save(newAccount);

        } else {
            // Handle the case where the user is not found
            throw new RuntimeException("User with email " + email + " not found");
        }

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

    @Override
    public void addBankUserDetails(BankDetailsDTO bankDto, String emailId) {
        Optional<User> userOptional = userRepo.findUserByEmail(emailId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            BankDetails bankDetails = new BankDetails();

            bankDetails.setAccHolderName(bankDto.getName());
            bankDetails.setAccNumber(bankDto.getAccNumber());
            bankDetails.setBankBranch(bankDto.getBranchName());
//			System.out.println("ifsc code: " + bankDto.getIFSCCode());
            bankDetails.setIFSCCode(bankDto.getIfsc());
            System.out.println(bankDto.getIfsc());
//			bankDetails.setIFSCCode(bankDto.getIfsc());
            user.setBankDetails(bankDetails);
            // userRepo.save(user);
            bankDetails.setUser(user);
            bankRepo.save(bankDetails);
        } else {
            // when the user is not found
            System.out.println("User with email " + emailId + " not found");
        }
    }
}
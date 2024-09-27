package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.InvalidInputException;
import com.example.exception.UsernameAlreadyExistsException;
import com.example.repository.AccountRepository;
import java.util.Optional;

@Service
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;

    // handle registration
    public Account registerAccount(String username, String password) throws InvalidInputException, UsernameAlreadyExistsException {
        // blank username
        if (username == null || username.trim().isEmpty()) {
            throw new InvalidInputException ("Username cannot be blank");
        }
        
        // password must be 4 characters
        if (password == null || password.length() < 4) {
            throw new InvalidInputException ("Password must be at least 4 characters long");
        }

        // no duplicate username
        Optional<Account> existingAccount = accountRepository.findByUsername(username);
        if (existingAccount.isPresent()) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        // create and save 
        Account newAccount = new Account(username, password);
        return accountRepository.save(newAccount);
    }

    // account login
    public Account accountLogin(String username, String password) throws InvalidInputException {
        Optional<Account> optionalAccount = accountRepository.findByUsername(username);

        // check to see if username exists
        if(optionalAccount.isEmpty()) {
            throw new InvalidInputException("Username Does not Exist");
        }
        Account account = optionalAccount.get();
        // check password
        if(!account.getPassword().equals(password)) {
            throw new InvalidInputException("Incorrect password");
        }
        // successfull
        return account;
    }
}
package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import java.util.Optional;

@Service
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;

    // handle registration
    public Account registerAccount(String username, String password) throws Exception {
        // blank username
        if (username == null || username.trim().isEmpty()) {
            throw new Exception("Username cannot be blank");
        }
        
        // password must be 4 characters
        if (password == null || password.length() < 4) {
            throw new Exception("Password must be at least 4 characters long");
        }

        // no duplicate username
        Optional<Account> existingAccount = accountRepository.findByUsername(username);
        if (existingAccount.isPresent()) {
            throw new Exception("Username already exists");
        }

        // create and save 
        Account newAccount = new Account(username, password);
        return accountRepository.save(newAccount);
    }
}
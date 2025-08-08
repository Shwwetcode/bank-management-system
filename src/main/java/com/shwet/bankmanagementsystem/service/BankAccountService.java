package com.shwet.bankmanagementsystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shwet.bankmanagementsystem.model.BankAccount;
import com.shwet.bankmanagementsystem.repository.BankAccountRepository;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository repository;

    public BankAccount createAccount(BankAccount account) {
        return repository.save(account);
    }

    public List<BankAccount> getAllAccounts() {
        return repository.findAll();
    }

    public Optional<BankAccount> getAccountById(Long id) {
        return repository.findById(id);
    }

    public BankAccount updateAccount(Long id, BankAccount updatedAccount) {
        return repository.findById(id).map(account -> {
            account.setHolderName(updatedAccount.getHolderName());
            account.setBalance(updatedAccount.getBalance());
            return repository.save(account);
        }).orElse(null);
    }

    public boolean deleteAccount(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
    public BankAccount deposit(Long accountId, double amount) {
    BankAccount account = repository.findById(accountId)
            .orElseThrow(() -> new RuntimeException("Account not found"));

    double newBalance = account.getBalance() + amount;
    account.setBalance(newBalance);

    return repository.save(account);
}
}
package com.shwet.bankmanagementsystem.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shwet.bankmanagementsystem.model.BankAccount;
import com.shwet.bankmanagementsystem.model.Transaction;
import com.shwet.bankmanagementsystem.repository.BankAccountRepository;
import com.shwet.bankmanagementsystem.repository.TransactionRepository;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public BankAccount createAccount(BankAccount account) {
        return bankAccountRepository.save(account);
    }

    public List<BankAccount> getAllAccounts() {
        return bankAccountRepository.findAll();
    }

    public Optional<BankAccount> getAccountById(Long id) {
        return bankAccountRepository.findById(id);
    }

    public BankAccount updateAccount(Long id, BankAccount updatedAccount) {
        BankAccount account = bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setHolderName(updatedAccount.getHolderName());
        account.setBalance(updatedAccount.getBalance());
        return bankAccountRepository.save(account);
    }

    public void deleteAccount(Long id) {
        bankAccountRepository.deleteById(id);
    }

    public BankAccount deposit(Long id, double amount) {
        BankAccount account = bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setBalance(account.getBalance() + amount);
        bankAccountRepository.save(account);

        // Log transaction
        logTransaction(account, "DEPOSIT", amount);

        return account;
    }

    public BankAccount withdraw(Long id, double amount) {
        BankAccount account = bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance() - amount);
        bankAccountRepository.save(account);

        // Log transaction
        logTransaction(account, "WITHDRAW", amount);

        return account;
    }

    private void logTransaction(BankAccount account, String type, double amount) {
        Transaction transaction = new Transaction();
        transaction.setBankAccount(account);
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }
}
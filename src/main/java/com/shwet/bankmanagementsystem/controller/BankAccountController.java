package com.shwet.bankmanagementsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shwet.bankmanagementsystem.model.BankAccount;
import com.shwet.bankmanagementsystem.service.BankAccountService;

@RestController
@RequestMapping("/accounts")
public class BankAccountController {

    @Autowired
    private BankAccountService service;

    // 📥 Create a new account
    @PostMapping
    public ResponseEntity<BankAccount> createAccount(@RequestBody BankAccount account) {
        return ResponseEntity.ok(service.createAccount(account));
    }

    // 📃 Get all accounts
    @GetMapping
    public ResponseEntity<List<BankAccount>> getAllAccounts() {
        return ResponseEntity.ok(service.getAllAccounts());
    }
    // 🔍 Get account by ID
@GetMapping("/{id}")
public ResponseEntity<BankAccount> getAccountById(@PathVariable Long id) {
    return service.getAccountById(id)
        .map(ResponseEntity::ok)  // If present, return 200 OK with the account
        .orElseGet(() -> ResponseEntity.notFound().build());  // If not found, return 404
}

    // ✏️ Update account info
    @PutMapping("/{id}")
    public ResponseEntity<BankAccount> updateAccount(@PathVariable Long id, @RequestBody BankAccount updatedAccount) {
        return ResponseEntity.ok(service.updateAccount(id, updatedAccount));
    }

    // ❌ Delete account
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        service.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    // 💰 Deposit money
    @PutMapping("/{id}/deposit")
public ResponseEntity<BankAccount> deposit(@PathVariable Long id, @RequestParam double amount) {
    try {
        BankAccount updated = service.deposit(id, amount);
        return ResponseEntity.ok(updated);
    } catch (RuntimeException e) {
        return ResponseEntity.notFound().build();
    }
}

    // (🚧 In next step) Withdraw money...
}
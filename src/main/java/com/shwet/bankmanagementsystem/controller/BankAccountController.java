package com.shwet.bankmanagementsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shwet.bankmanagementsystem.model.BankAccount;
import com.shwet.bankmanagementsystem.model.Transaction;
import com.shwet.bankmanagementsystem.repository.TransactionRepository;
import com.shwet.bankmanagementsystem.service.BankAccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/accounts")
@Tag(name = "Bank Account API", description = "Endpoints for managing bank accounts")
public class BankAccountController {

    @Autowired
    private BankAccountService service;

    @Autowired
    private TransactionRepository transactionRepository;

    // ✅ Create Account
    @Operation(
            summary = "Create a new bank account",
            description = "Adds a new account to the system",
            requestBody = @RequestBody(
                    description = "Account details",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Example Account",
                                    value = "{ \"holderName\": \"John Doe\", \"balance\": 5000.0 }"
                            )
                    )
            )
    )
    @ApiResponse(responseCode = "200", description = "Account created successfully")
    @PostMapping
    public ResponseEntity<BankAccount> createAccount(@org.springframework.web.bind.annotation.RequestBody BankAccount account) {
        return ResponseEntity.ok(service.createAccount(account));
    }

    // ✅ Get All Accounts
    @Operation(summary = "Get all accounts", description = "Retrieves a list of all bank accounts")
    @ApiResponse(responseCode = "200", description = "List of accounts retrieved")
    @GetMapping
    public ResponseEntity<List<BankAccount>> getAllAccounts() {
        return ResponseEntity.ok(service.getAllAccounts());
    }

    // ✅ Get Account by ID
    @Operation(summary = "Get account by ID", description = "Retrieves account details for a given ID")
    @ApiResponse(responseCode = "200", description = "Account found")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @GetMapping("/{id}")
    public ResponseEntity<BankAccount> getAccountById(@PathVariable Long id) {
        return service.getAccountById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Update Account
    @Operation(
            summary = "Update account",
            description = "Updates details of an existing account by ID",
            requestBody = @RequestBody(
                    description = "Updated account details",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Example Update",
                                    value = "{ \"holderName\": \"Jane Doe\", \"balance\": 7500.0 }"
                            )
                    )
            )
    )
    @ApiResponse(responseCode = "200", description = "Account updated successfully")
    @PutMapping("/{id}")
    public ResponseEntity<BankAccount> updateAccount(@PathVariable Long id, @org.springframework.web.bind.annotation.RequestBody BankAccount updatedAccount) {
        return ResponseEntity.ok(service.updateAccount(id, updatedAccount));
    }

    // ✅ Delete Account
    @Operation(summary = "Delete account", description = "Deletes a bank account by ID")
    @ApiResponse(responseCode = "204", description = "Account deleted successfully")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        service.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Deposit
    @Operation(summary = "Deposit money", description = "Deposits a given amount into the account")
    @ApiResponse(responseCode = "200", description = "Deposit successful")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @PutMapping("/{id}/deposit")
    public ResponseEntity<BankAccount> deposit(@PathVariable Long id, @RequestParam double amount) {
        try {
            BankAccount updated = service.deposit(id, amount);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Withdraw
    @Operation(summary = "Withdraw money", description = "Withdraws a given amount from the account")
    @PutMapping("/{id}/withdraw")
    public ResponseEntity<BankAccount> withdraw(@PathVariable Long id, @RequestParam double amount) {
        try {
            BankAccount updated = service.withdraw(id, amount);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // ✅ Get Transaction History
    @Operation(summary = "Get transaction history", description = "Retrieves all transactions for a given account ID")
    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable Long id) {
        return ResponseEntity.ok(transactionRepository.findByBankAccountId(id));
    }
}
package com.hori.managemoney.core.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hori.managemoney.persistence.entity.Transaction;
import com.hori.managemoney.persistence.filter.TransactionFilter;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<?> getTransaction(TransactionFilter transactionFilter) {
        return transactionService.getTransactions(transactionFilter);
    }

    @PostMapping
    public ResponseEntity<?> createTransaction(
        @RequestBody Transaction transaction
    ) {
        return transactionService.createTransaction(transaction);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateTransaction(
        @RequestBody Transaction transaction
    ) {
        return transactionService.updateTransaction(transaction);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTransaction(
        @RequestParam("id") String id) {
        return transactionService.deleteTransaction(id);
    }
}

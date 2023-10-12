package com.hori.managemoney.core.transaction;

import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hori.managemoney.persistence.dao.AccountCodeDao;
import com.hori.managemoney.persistence.dao.TransactionDao;
import com.hori.managemoney.persistence.entity.AccountCode;
import com.hori.managemoney.persistence.entity.Transaction;
import com.hori.managemoney.persistence.entity.User;
import com.hori.managemoney.persistence.filter.TransactionFilter;

@Service
public class TransactionService {
    
    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    private AccountCodeDao accountCodeDao;

    private final int MAX_CHARACTERS = 255;

    public ResponseEntity<?> getTransactions(TransactionFilter transactionFilter) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        transactionFilter.setUsername(user.getUsername());
        List<Transaction> transactions = transactionDao.getByFilter(transactionFilter);
        if (transactions == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(transactions);
    }

    public ResponseEntity<?> createTransaction(Transaction transaction) {
        if (Strings.isBlank(transaction.getAccountCode()) || transaction.getAccountCode().length() > MAX_CHARACTERS) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (Strings.isBlank(transaction.getNote()) || transaction.getNote().length() > MAX_CHARACTERS) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Relation: Transaction depends on Account Code
        AccountCode accountCode = accountCodeDao.getById(transaction.getAccountCode());
        if (accountCode == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        transaction.setUsername(user.getUsername());
        String id = transactionDao.insert(transaction);
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        transaction.setId(id);
        return ResponseEntity.ok(transaction);
    }

    public ResponseEntity<?> updateTransaction(Transaction transaction) {
        if (Strings.isBlank(transaction.getNote()) || transaction.getNote().length() > MAX_CHARACTERS) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Transaction prevTransaction = transactionDao.getById(transaction.getId());

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (prevTransaction == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (!prevTransaction.getUsername().equals(user.getUsername())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Boolean success = transactionDao.update(transaction);
        return (success) ? ResponseEntity.ok(success) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Boolean> deleteTransaction(String id) {
        if (Strings.isBlank(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Transaction transaction = transactionDao.getById(id);
        if (transaction == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (!transaction.getUsername().equals(user.getUsername())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Boolean success = transactionDao.delete(transaction);
        return (success) ? ResponseEntity.ok(success) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

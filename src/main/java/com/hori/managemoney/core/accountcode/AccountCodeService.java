package com.hori.managemoney.core.accountcode;

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
import com.hori.managemoney.persistence.entity.User;
import com.hori.managemoney.persistence.filter.AccountCodeFilter;

@Service
public class AccountCodeService {
    
    @Autowired
    private AccountCodeDao accountCodeDao;

    @Autowired
    private TransactionDao transactionDao;

    private final int MAX_CHARACTERS = 255;

    public ResponseEntity<?> getAccountCode(AccountCodeFilter accountCodeFilter) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        accountCodeFilter.setUsername(user.getUsername());
        List<AccountCode> accountCodes = accountCodeDao.getByFilter(accountCodeFilter);
        if (accountCodes == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(accountCodes);
    }

    public ResponseEntity<?> createAccountCode(AccountCode accountCode) {
        if (Strings.isBlank(accountCode.getName()) || accountCode.getName().length() > MAX_CHARACTERS) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        accountCode.setUsername(user.getUsername());
        String id = accountCodeDao.insert(accountCode);
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        accountCode.setId(id);
        return ResponseEntity.ok(accountCode);
    }

    public ResponseEntity<?> updateAccountCode(AccountCode accountCode) {
        if (Strings.isBlank(accountCode.getName()) || accountCode.getName().length() > MAX_CHARACTERS) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AccountCode prevAccountCode = accountCodeDao.getById(accountCode.getId());
        
        if (prevAccountCode == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } if (!prevAccountCode.getUsername().equals(user.getUsername())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Boolean success = accountCodeDao.update(accountCode);
        return (success) ? ResponseEntity.ok(success) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> deleteAccountCode(String id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (Strings.isBlank(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AccountCode accountCode = accountCodeDao.getById(id);
        if (accountCode == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (!accountCode.getUsername().equals(user.getUsername())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // Relation: Transaction depends on Account Code
        Boolean hasTransaction = transactionDao.transactionExistsWithCode(id);
        if (hasTransaction == null || hasTransaction) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Boolean success = accountCodeDao.delete(accountCode);
        return (success) ? ResponseEntity.ok(success) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

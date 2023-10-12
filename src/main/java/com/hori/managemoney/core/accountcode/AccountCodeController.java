package com.hori.managemoney.core.accountcode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hori.managemoney.persistence.entity.AccountCode;
import com.hori.managemoney.persistence.filter.AccountCodeFilter;

@RestController
@RequestMapping("/api/accountcode")
public class AccountCodeController {
    
    @Autowired
    private AccountCodeService accountCodeService;

    @GetMapping
    public ResponseEntity<?> getAccountCode(AccountCodeFilter accountCodeFilter) {
        return accountCodeService.getAccountCode(accountCodeFilter);
    }

    @PostMapping
    public ResponseEntity<?> createAccountCode(
        @RequestBody AccountCode accountCode
    ) {
        return accountCodeService.createAccountCode(accountCode);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateTransaction(
        @RequestBody AccountCode accountCode
    ) {
        return accountCodeService.updateAccountCode(accountCode);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAccountCode(
        @RequestParam("id") String id) {
        return accountCodeService.deleteAccountCode(id);
    }
}

package com.hori.managemoney.persistence.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.hori.managemoney.persistence.entity.Transaction;

@Component
public class TransactionMapper implements RowMapper<Transaction> {
    @Override
    public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
        var transaction = new Transaction();
        transaction.setId(rs.getString("id"));
        transaction.setCreatedAt(rs.getTimestamp("created_at"));
        transaction.setCreatedBy(rs.getString("created_by"));
        transaction.setUpdatedAt(rs.getTimestamp("updated_at"));
        transaction.setUpdatedBy(rs.getString("updated_by"));
        transaction.setRecordFlag(rs.getString("record_flag"));
        transaction.setAccountCode(rs.getString("account_code"));
        transaction.setNote(rs.getString("note"));
        transaction.setAmount(rs.getBigDecimal("amount"));
        transaction.setDatetime(rs.getTimestamp("datetime"));
        transaction.setUsername(rs.getString("username"));
        return transaction;
    }
}



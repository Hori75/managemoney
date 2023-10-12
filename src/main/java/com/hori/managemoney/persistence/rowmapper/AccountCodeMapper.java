package com.hori.managemoney.persistence.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.hori.managemoney.persistence.entity.AccountCode;

@Component
public class AccountCodeMapper implements RowMapper<AccountCode> {
    @Override
    public AccountCode mapRow(ResultSet rs, int rowNum) throws SQLException {
        var accountCode = new AccountCode();
        accountCode.setId(rs.getString("id"));
        accountCode.setCreatedAt(rs.getTimestamp("created_at"));
        accountCode.setCreatedBy(rs.getString("created_by"));
        accountCode.setUpdatedAt(rs.getTimestamp("updated_at"));
        accountCode.setUpdatedBy(rs.getString("updated_by"));
        accountCode.setRecordFlag(rs.getString("record_flag"));
        accountCode.setName(rs.getString("name"));
        accountCode.setUsername(rs.getString("username"));
        return accountCode;
    }
}



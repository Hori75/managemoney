package com.hori.managemoney.persistence.dao;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.hori.managemoney.persistence.entity.AccountCode;
import com.hori.managemoney.persistence.filter.AccountCodeFilter;
import com.hori.managemoney.persistence.rowmapper.AccountCodeMapper;

@Component
public class AccountCodeDao extends BaseEntityDao<AccountCode, AccountCodeFilter> {

    @Autowired
    public AccountCodeDao(NamedParameterJdbcTemplate jdbcTemplate, AccountCodeMapper mapper) {
        super(jdbcTemplate, mapper, LoggerFactory.getLogger(AccountCodeDao.class));
    }

    @Override
    protected String selectSql() {
        return "SELECT " + BASE_ENTITY_COLUMN
            + ", name as name, "
            + " username as username, "
            + " FROM " + tableName();
    }

    @Override
    protected String insertSql() {
        return "INSERT INTO " + tableName()
            + " ( " + BASE_ENTITY_COLUMN + ", "
            + "name, "
            + "username, "
            + ") "
            + "VALUES "
            + " ( " + BASE_ENTITY_PARAMETER + ", "
            + " :name, :username "
            + ")";
    }

    @Override
    protected String updateSql() {
        return "UPDATE " + tableName()
            + " SET "
            + BASE_ENTITY_UPDATE
            + " name = COALESCE(:name, name), "
            + " WHERE id = :id";
    }

    @Override
    public String tableName() {
        return " account_code ";
    }
}


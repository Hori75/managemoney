package com.hori.managemoney.persistence.dao;

import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hori.managemoney.persistence.entity.Transaction;
import com.hori.managemoney.persistence.filter.TransactionFilter;
import com.hori.managemoney.persistence.rowmapper.TransactionMapper;

@Component
public class TransactionDao extends BaseEntityDao<Transaction, TransactionFilter> {

    @Autowired
    public TransactionDao(NamedParameterJdbcTemplate jdbcTemplate, TransactionMapper mapper) {
        super(jdbcTemplate, mapper, LoggerFactory.getLogger(TransactionDao.class));
    }

    @Override
    protected String selectSql() {
        return "SELECT " + BASE_ENTITY_COLUMN
            + ", account_code AS account_code, "
            + " note AS note,"
            + " amount AS amount, "
            + " datetime AS datetime, "
            + " username AS username "
            + " FROM " + tableName();
    }

    @Override
    protected String insertSql() {
        return "INSERT INTO " + tableName()
            + " ( " + BASE_ENTITY_COLUMN + ", "
            + "account_code, "
            + "note, "
            + "amount, "
            + "datetime, "
            + "username, "
            + ") "
            + "VALUES "
            + " ( " + BASE_ENTITY_PARAMETER + ", "
            + " :account_code, :note, :amount, :datetime, :username "
            + ")";
    }

    @Override
    protected String updateSql() {
        return "UPDATE " + tableName()
            + " SET "
            + BASE_ENTITY_UPDATE
            + " note = COALESCE(:note, note), "
            + " amount = COALESCE(:amount, amount), "
            + " datetime = COALESCE(:datetime, datetime) "
            + " WHERE id = :id";
    }

    @Override
    public String tableName() {
        return " transaction ";
    }

    public Boolean transactionExistsWithCode(String accountCode) {
        String sql = "SELECT EXISTS(SELECT 1 FROM " + tableName() + " WHERE account_code = :account_code )";
        TransactionFilter filter = new TransactionFilter();
        filter.setAccountCode(accountCode);
        Map<String, Object> paramMap = mapper.convertValue(filter, new TypeReference<>() {});
        try {
            return jdbcTemplate.queryForObject(sql, paramMap, Boolean.class);
        } catch (DataAccessException e) {
            logger.error("Update data access error", e);
            return null;
        }
    }

}


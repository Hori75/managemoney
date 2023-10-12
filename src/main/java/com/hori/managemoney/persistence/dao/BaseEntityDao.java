package com.hori.managemoney.persistence.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hori.managemoney.persistence.entity.BaseEntity;
import com.hori.managemoney.persistence.filter.Filter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.StringUtils;

public abstract class BaseEntityDao<T extends BaseEntity, F extends Filter> {

    protected final NamedParameterJdbcTemplate jdbcTemplate;
    protected final RowMapper<T> rowMapper;
    protected abstract String selectSql();
    protected abstract String insertSql();
    protected abstract String updateSql();
    protected static final String BASE_ENTITY_COLUMN =
        " id, created_at, created_by, updated_at, updated_by, record_flag ";
    protected static final String BASE_ENTITY_PARAMETER =
        " :id, timezone('Asia/Jakarta', NOW()), :created_by, "
            + " timezone('Asia/Jakarta', NOW()), :updated_by, :record_flag ";
    protected static final String BASE_ENTITY_UPDATE = 
        " updated_by = COALESCE(:updated_by, updated_by), "
        + " updated_at = timezone('Asia/Jakarta', NOW()), ";
    protected ObjectMapper mapper;
    protected final Logger logger;
    public abstract String tableName();

    protected BaseEntityDao(NamedParameterJdbcTemplate jdbcTemplate,
                            RowMapper<T> rowMapper,
                            Logger logger) {
        this.jdbcTemplate = jdbcTemplate; 
        this.rowMapper = rowMapper;
        this.logger = logger;
    }

    @Autowired
    public final void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    protected String countSql() {
        return "SELECT COUNT(id) FROM " + tableName();
    }

    public T getById(String id) {
        String sql = selectSql() + " WHERE id = :id";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);
        try {
            return jdbcTemplate.queryForObject(sql, paramMap, rowMapper);
        } catch (DataAccessException e) {
            logger.error("Select data access error", e);
            return null;
        }
    }

    public T getOneByFilter(Filter filter) {
        Map<String, Object> paramMap = mapper.convertValue(filter, new TypeReference<>() {});
        String sql = selectSql() + createFilterQuery(paramMap);

        try {
            return jdbcTemplate.queryForObject(sql, paramMap, rowMapper);
        } catch (DataAccessException e) {
            logger.error("Select data access error", e);
            return null;
        }
    }

    public List<T> getByFilter(Filter filter) {
        Map<String, Object> paramMap = mapper.convertValue(filter, new TypeReference<>() {});
        String sql = selectSql() + createFilterQuery(paramMap);

        try {
            return jdbcTemplate.query(sql, paramMap, rowMapper);
        } catch (DataAccessException e) {
            logger.error("Select data access error", e);
            return null;
        }
    }

    public String insert(T entity) {
        var id = UUID.randomUUID().toString();
        String sql = insertSql();
        Map<String, Object> paramMap = mapper.convertValue(entity, new TypeReference<>() {});

        if (!StringUtils.hasText((String) paramMap.get("id"))) {
            paramMap.put("id", id);
        }
        if (!StringUtils.hasText((String) paramMap.get("created_by"))) {
            paramMap.put("created_by", "system");
        }
        if (!StringUtils.hasText((String) paramMap.get("updated_by"))) {
            paramMap.put("updated_by", "system");
        }

        try {
            jdbcTemplate.update(sql, paramMap);
            return id;
        } catch (DataAccessException e) {
            logger.error("Insert data Access error", e);
            return null;
        }
    }

    public boolean update(T entity) {
        String sql = updateSql();
        Map<String, Object> paramMap = mapper.convertValue(entity, new TypeReference<>() {});
        try {
            jdbcTemplate.update(sql, paramMap);
            return true;
        } catch (DataAccessException e) {
            logger.error("Update data access error", e);
            return false;
        }
    }

    public boolean delete(T entity) {
        String sql = "UPDATE " + tableName()
            + " SET "
            + BASE_ENTITY_UPDATE
            + " record_flag = :record_flag "
            + " WHERE id = :id";
        Map<String, Object> paramMap = mapper.convertValue(entity, new TypeReference<>() {});
        try {
            jdbcTemplate.update(sql, paramMap);
            return true;
        } catch (DataAccessException e) {
            logger.error("Update data access error", e);
            return false;
        }
    }

    public String createFilterQuery(Map<String, Object> paramMap) {
        String filterSql = " WHERE true ";

        if (StringUtils.hasText((String) paramMap.get("id"))) {
            filterSql += " AND id = :id AND record_flag = :record_flag ";
        }else {
            for (var entry : paramMap.entrySet()) {
                String rowName = entry.getKey();
                String operand = " = ";
                if (entry.getKey().endsWith("highest") || entry.getKey().endsWith("before")) {
                    operand = " <= ";
                    rowName.replaceAll("(_before|_highest)", "");
                }
                if (entry.getKey().endsWith("lowest") || entry.getKey().endsWith("after")) {
                    operand = " >= ";
                    rowName.replaceAll("(_after|_lowest)", "");
                }
                if (entry.getValue() != null) {
                    filterSql += " AND " + rowName + operand + ":" + entry.getKey() + " ";
                }
            }
        }

        return filterSql;
    }

}


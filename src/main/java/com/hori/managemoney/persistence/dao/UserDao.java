package com.hori.managemoney.persistence.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hori.managemoney.auth.UserLoginForm;
import com.hori.managemoney.persistence.entity.User;
import com.hori.managemoney.persistence.filter.UserFilter;
import com.hori.managemoney.persistence.rowmapper.UserMapper;

@Component
public class UserDao extends BaseEntityDao<User, UserFilter> {
    @Autowired
    public UserDao(NamedParameterJdbcTemplate jdbcTemplate, UserMapper mapper) {
        super(jdbcTemplate, mapper, LoggerFactory.getLogger(UserDao.class));
    }

    @Override
    protected String selectSql() {
        return "SELECT " + BASE_ENTITY_COLUMN
            + ", username AS username, "
            + " first_name AS firstName,"
            + " last_name AS lastName, "
            + " email AS email, "
            + " password AS password "
            + " FROM " + tableName();
    }

    @Override
    protected String insertSql() {
        return "INSERT INTO " + tableName()
            + " ( " + BASE_ENTITY_COLUMN + ", "
            + "username, "
            + "first_name, "
            + "last_name, "
            + "email, "
            + "password "
            + ") "
            + "VALUES "
            + " ( " + BASE_ENTITY_PARAMETER + ", "
            + " :username, :first_name, :last_name, :email, :password "
            + ")";
    }

    @Override
    protected String updateSql() {
        return "UPDATE " + tableName()
            + " SET "
            + BASE_ENTITY_UPDATE
            + " first_name = COALESCE(:firstName, first_name), "
            + " last_name = COALESCE(:lastName, last_name), "
            + " password = COALESCE(:password, password) "
            + " WHERE id = :id";
    }

    @Override
    public String tableName() {
        return " users ";
    }

    public User getUserByCredentials(UserLoginForm userLoginForm) {
        String sql = selectSql() 
        + " WHERE username = :username " 
        + " AND password = :password ";
        Map<String, Object> paramMap = mapper.convertValue(userLoginForm, new TypeReference<>() {});

        try {
            return jdbcTemplate.queryForObject(sql, paramMap, rowMapper);
        } catch (DataAccessException e) {
            logger.error("Select data access error", e);
            return null;
        }
    }

    public User getUserByUsername(String username) {
        UserFilter filter = new UserFilter();
        filter.setUsername(username);
        List<User> result = getByFilter(filter);
        if (result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }

}

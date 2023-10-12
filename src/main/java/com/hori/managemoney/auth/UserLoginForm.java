package com.hori.managemoney.auth;

import org.apache.logging.log4j.util.Strings;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserLoginForm {
    private String username;
    private String password;

    final int MAX_CHARACTERS = 55;
    final int MAX_EMAIL_CHARACTERS = 255;

    public boolean validate() {
        if (Strings.isBlank(username) || username.length() > MAX_CHARACTERS) {
            return false;
        }
        if (Strings.isBlank(password) || password.length() > MAX_EMAIL_CHARACTERS) {
            return false;
        }

        return true;
    }
}

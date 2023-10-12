package com.hori.managemoney.auth;

import org.apache.logging.log4j.util.Strings;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hori.managemoney.persistence.entity.User;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserForm {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;
    
    final int MAX_CHARACTERS = 55;
    final int MAX_EMAIL_CHARACTERS = 255;

    public User toUserEntity() {
        User user = new User();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }

    public boolean validate() {
        if (Strings.isBlank(username) || username.length() > MAX_CHARACTERS) {
            return false;
        }
        if (Strings.isBlank(firstName) || firstName.length() > MAX_CHARACTERS) {
            return false;
        }
        if (Strings.isBlank(lastName) || lastName.length() > MAX_CHARACTERS) {
            return false;
        }
        if (Strings.isBlank(email) || email.length() > MAX_EMAIL_CHARACTERS) {
            return false;
        }
        if (Strings.isBlank(password) || password.length() > MAX_EMAIL_CHARACTERS) {
            return false;
        }
        if (!password.equals(confirmPassword)) {
            return false;
        }

        return true;
    }
}

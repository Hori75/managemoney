package com.hori.managemoney.auth;

import java.util.HashMap;
import java.util.Map;

import com.hori.managemoney.auth.utils.JwtTokenUtil;
import com.hori.managemoney.persistence.dao.UserDao;
import com.hori.managemoney.persistence.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class AuthService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public ResponseEntity<?> userLogin(UserLoginForm loginForm) {
        if (!loginForm.validate()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User loginUser = userDao.getUserByCredentials(loginForm);

        if (loginUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        final String jwtToken = jwtTokenUtil.generateToken(loginUser);
        Map<String, String> tokenResp = new HashMap<>();
        tokenResp.put("token", jwtToken);

        return ResponseEntity.ok(tokenResp);
    }

    public ResponseEntity<?> userRegister(UserForm userForm) {
        if (!userForm.validate()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String id = userDao.insert(userForm.toUserEntity());
        
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(id);
    }

    public ResponseEntity<?> getUser(String id) {
        return ResponseEntity.ok(userDao.getById(id));
    }

    public ResponseEntity<?> updateUser(UserForm userForm, String userId) {
        if (!userForm.validate()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userForm.toUserEntity();
        user.setId(userId);
        if (userDao.update(user)) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.badRequest().body(false);
        }
    }

    
}


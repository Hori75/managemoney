package com.hori.managemoney.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hori.managemoney.persistence.entity.User;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
        @RequestBody UserLoginForm loginForm
    ) {
        return authService.userLogin(loginForm);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
        @RequestBody UserForm userForm
    ) {
        return authService.userRegister(userForm);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserData() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return authService.getUser(user.getId());
    }    
}

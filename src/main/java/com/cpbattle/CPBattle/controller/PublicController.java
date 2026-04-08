package com.cpbattle.CPBattle.controller;

import com.cpbattle.CPBattle.entity.User;
import com.cpbattle.CPBattle.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    AuthService authService;  //IMPORT AUTH SERVICE

    // SIGN UP USER
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody User user) {
        //sign up logic
        return authService.signup(user);
    }

    //LOGIN USER
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user){
        //login logic
        return authService.login(user);
    }

}

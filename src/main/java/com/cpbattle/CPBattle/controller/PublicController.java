package com.cpbattle.CPBattle.controller;

import com.cpbattle.CPBattle.entity.Question;
import com.cpbattle.CPBattle.entity.User;
import com.cpbattle.CPBattle.service.AuthService;
import com.cpbattle.CPBattle.service.QuestionService;
import com.cpbattle.CPBattle.service.QuestionSyncService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    AuthService authService;  //IMPORT AUTH SERVICE
    @Autowired
    QuestionSyncService questionSyncService;

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


    @GetMapping("/import")
    public String importQuestions() throws Exception{
        questionSyncService.syncQuestions();
        return "Question imported successfully!";
    }
}

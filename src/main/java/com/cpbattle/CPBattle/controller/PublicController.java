package com.cpbattle.CPBattle.controller;

import com.cpbattle.CPBattle.entity.Question;
import com.cpbattle.CPBattle.entity.User;
import com.cpbattle.CPBattle.service.AuthService;
import com.cpbattle.CPBattle.service.QuestionService;
import lombok.Getter;
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

//    @Getter
//    public static class Input{
//        Integer rating;
//        String username1;
//        String username2;
//    }
//
//    @PostMapping("/get-question")
//    public ResponseEntity<Question> getQestion(@RequestBody Input input){
//        return questionService.findQuestionWithRating(input.getRating(), input.getUsername1(), input.getUsername2());
//    }
}

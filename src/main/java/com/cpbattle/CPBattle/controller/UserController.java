package com.cpbattle.CPBattle.controller;

import com.cpbattle.CPBattle.entity.User;
import com.cpbattle.CPBattle.service.UserDetailsServiceImpl;
import com.cpbattle.CPBattle.service.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public User getUser(){
        return userService.getUser();
    }

    @GetMapping("/friends")
    public ResponseEntity<?> getFriends(){
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getFriends(username);
    }
    @PostMapping("/add-friend/{friendUsername}")
    public ResponseEntity<?> addFriend(@PathVariable String friendUsername){
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.addFriend(username, friendUsername);
    }
}

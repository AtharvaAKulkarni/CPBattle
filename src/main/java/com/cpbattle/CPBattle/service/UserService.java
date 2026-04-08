package com.cpbattle.CPBattle.service;

import com.cpbattle.CPBattle.entity.User;
import com.cpbattle.CPBattle.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> getFriends(String username) {
        User user=userRepository.findByUsername(username);
        List<String> friends=user.getFriends();
        if(friends!=null){
            return ResponseEntity.ok(friends);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Friends not found");
    }

    public ResponseEntity<?> addFriend(String username, String friendUsername) {
        User user=userRepository.findByUsername(username);
        User friend=userRepository.findByUsername(friendUsername);
        if(user!=null && friend!=null){
            if (!user.getFriends().contains(friendUsername)) {
                user.getFriends().add(friendUsername);
                userRepository.save(user);
            }
            return ResponseEntity.ok("Friend Added");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not add friend");
    }
}

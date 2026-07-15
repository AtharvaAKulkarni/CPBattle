package com.cpbattle.CPBattle.service;

import com.cpbattle.CPBattle.DTO.LeaderboardDTO;
import com.cpbattle.CPBattle.entity.User;
import com.cpbattle.CPBattle.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void updateScore(List<LeaderboardDTO> leaderboard) {
        if(leaderboard.getFirst().getScore()!=0){
            User winner= userRepository.findByUsername(leaderboard.getFirst().getUsername());
            winner.setBattlesWon(winner.getBattlesWon()+1);
            userRepository.save(winner);
            for(LeaderboardDTO user:leaderboard){
                if(user.equals(leaderboard.getFirst())) continue;
                User u=userRepository.findByUsername(user.getUsername());
                u.setBattlesLost(u.getBattlesLost()+1);
                userRepository.save(u);
            }
            return;
        }
        for(LeaderboardDTO user:leaderboard) {
            User u = userRepository.findByUsername(user.getUsername());
            u.setBattlesTied(u.getBattlesTied() + 1);
            userRepository.save(u);
        }
    }

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

    public User getUser(){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username);
    }
}

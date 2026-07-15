package com.cpbattle.CPBattle.service;

import com.cpbattle.CPBattle.entity.User;
import com.cpbattle.CPBattle.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        username=username.toLowerCase();
        User user=userRepository.findByUsername(username);
        if(user!=null){
            return org.springframework.security.core.userdetails.User.builder().username(user.getUsername()).password(user.getPassword()).build();
        }
        throw new UsernameNotFoundException("User not found with username " + username);
    }
}

package com.cpbattle.CPBattle.service;

import com.cpbattle.CPBattle.entity.User;
import com.cpbattle.CPBattle.entity.VerifyUserResponse;
import com.cpbattle.CPBattle.repository.UserRepository;
import com.cpbattle.CPBattle.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.channels.UnresolvedAddressException;

@Service
public class AuthService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JWTUtil jwtUtil;
    private boolean verifyUser(User user) throws HttpClientErrorException {
        String url = "https://codeforces.com/api/user.info?handles={handle}&checkHistoricHandles=false";
//        url=url.replace("<handle>", user.getUsername());
        try{
            com.cpbattle.CPBattle.entity.VerifyUserResponse userResponse =restTemplate.getForObject(url, VerifyUserResponse.class, user.getUsername());
            return userResponse != null && "OK".equals(userResponse.getStatus());
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Codeforces API error");
        }
    }
    public ResponseEntity<?> signup(User user){
        if(userRepository.existsByUsername(user.getUsername())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }
        if(!verifyUser(user)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Codeforces Handle");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser=userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
    public ResponseEntity<?> login(User user){
        try{
            Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword()
            ));
            UserDetails userDetails=userDetailsService.loadUserByUsername(user.getUsername());
            String jwt= jwtUtil.generateToken(userDetails.getUsername());
            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Username or password");
        }
    }
}

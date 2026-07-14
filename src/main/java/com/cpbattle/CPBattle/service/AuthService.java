package com.cpbattle.CPBattle.service;

import com.cpbattle.CPBattle.entity.User;
import com.cpbattle.CPBattle.DTO.VerifyUserResponseDTO;
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

import java.util.HashMap;
import java.util.Map;

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
        System.out.println("Username: "+ user.getUsername());
        try{
            VerifyUserResponseDTO userResponse =restTemplate.getForObject(url, VerifyUserResponseDTO.class, user.getUsername());
            return userResponse != null && "OK".equals(userResponse.getStatus());
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Codeforces API error", e);
        }
    }
    public ResponseEntity<?> signup(User user){
        System.out.println(user.toString());
        if(userRepository.existsByUsername(user.getUsername())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }
        if(!verifyUser(user)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Codeforces Handle");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser=userRepository.save(user);
        return login(savedUser);
    }

    public ResponseEntity<?> login(User user){
        try{
            Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword()
            ));
            UserDetails userDetails=userDetailsService.loadUserByUsername(user.getUsername());
            String jwt= jwtUtil.generateToken(userDetails.getUsername());
            user=userRepository.findByUsername(user.getUsername());
            Map<String, Object> response=new HashMap<>();
            response.put("token", jwt);
            response.put("user", user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Username or password");
        }
    }
}

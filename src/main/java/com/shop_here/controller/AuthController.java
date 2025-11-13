package com.shop_here.controller;

import com.shop_here.model.User;
import com.shop_here.repository.UserRepository;
import com.shop_here.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public String signup(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);

        return "User Registered Successfully";
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody User user){
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            return ResponseEntity.badRequest().body("User Already Exist.");
        }

        User newUser = User.builder()
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .role("ROLE_USER")
                .build();

        userRepository.save(newUser);
        return ResponseEntity.ok("User registered successfully.");
    }

    @PostMapping("/login")
    public String login(@RequestBody User user){
        User dbUser = userRepository.findByEmail(user.getEmail()).orElse(null);
        if(dbUser != null && passwordEncoder.matches(user.getPassword(), dbUser.getPassword())){
            return jwtUtil.generateToken(user.getEmail());
        }

        return "Invalid Credentials!";
    }
}

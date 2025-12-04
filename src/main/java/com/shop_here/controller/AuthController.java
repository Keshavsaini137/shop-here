package com.shop_here.controller;

import com.shop_here.dto.AuthResponse;
import com.shop_here.dto.LoginRequest;
import com.shop_here.model.User;
import com.shop_here.repository.UserRepository;
import com.shop_here.security.JwtUtil;
import com.shop_here.service.CustomUserService;
import com.shop_here.utils.GlobalConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private CustomUserService customUserService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

//    @PostMapping("/signup")
//    public String signup(@RequestBody User user){
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setRole("ROLE_USER");
//        userRepository.save(user);
//
//        return "User Registered Successfully";
//    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody User user){
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            return ResponseEntity.badRequest().body("User Already Exist.");
        }

        User newUser = User.builder()
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .role(GlobalConstant.USER_ROLE)
                .build();

        userRepository.save(newUser);
        return ResponseEntity.ok("User registered successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        authManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()));
        UserDetails userDetails = customUserService.loadUserByUsername(loginRequest.getEmail());

        String toker = jwtUtil.generateToken(userDetails);

//        User dbUser = userRepository.findByEmail(user.getEmail()).orElse(null);
//        if(dbUser != null && passwordEncoder.matches(user.getPassword(), dbUser.getPassword())){
//            return jwtUtil.generateToken(user.getEmail());
//        }

        return ResponseEntity.ok(new AuthResponse(toker));
    }

}

package com.campoverde.AuthFeatures.controller;

/*Responsible for recieving actual HTTP requests*/

import com.campoverde.AuthFeatures.dto.EmailRequest;
import com.campoverde.AuthFeatures.dto.LoginRequest;
import com.campoverde.AuthFeatures.dto.RegisterRequest;
import com.campoverde.AuthFeatures.model.User;
import com.campoverde.AuthFeatures.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3002")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    //check is user was actually registered successfully or not
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        String result = userService.register(request);
        User user = new User(request.getUsername(), request.getPassword());
        user.setEmail(request.getEmail());
        if (result.startsWith("User registered successfully")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String token = userService.login(request);
        if (token.equals("User not found") || token.equals("Wrong password")) {
            return ResponseEntity.status(401).body(token);
        } else {
            return ResponseEntity.ok(token);
        }

    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        Optional<User> optionalUser = userService.verifyUser(token);
        if(optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }
        return ResponseEntity.ok("Email verified successfully!");
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<String> resendVerification(@RequestBody EmailRequest request) {
        userService.resendVerificationLink(request.getEmail());
        return ResponseEntity.ok("Verification link resent.");
    }

    @GetMapping("/is-verified")
    public ResponseEntity<Boolean> isVerified(@RequestParam String email) {
        boolean verified = userService.isEmailVerified(email);
        return ResponseEntity.ok(verified);
    }
}

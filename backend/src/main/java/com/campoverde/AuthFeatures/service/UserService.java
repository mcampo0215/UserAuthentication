package com.campoverde.AuthFeatures.service;

import com.campoverde.AuthFeatures.dto.LoginRequest;
import com.campoverde.AuthFeatures.dto.RegisterRequest;
import com.campoverde.AuthFeatures.model.User;
import com.campoverde.AuthFeatures.repository.UserRepository;
import com.campoverde.AuthFeatures.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/*contains logic needed to register a new user*/
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    //method to register a user
    public String register(RegisterRequest request) {
        //check if username already exists
        if(userRepository.findByUsername(request.getUsername()).isPresent()) {
            return "Username already exists";
        }
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            return "Email already in use";
        }
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        String verificationToken = UUID.randomUUID().toString();

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(hashedPassword);
        user.setVerificationToken(verificationToken);
        user.setVerified(false);
        userRepository.save(user); //save actual user

        String verificationUrl = "http://localhost:8080/api/auth/verify?token=" + verificationToken;

        emailService.sendEmail(
                request.getEmail(),
                "Verify Your Account",
                "Click the following link to verify your account: " + verificationUrl
        );
        return "User registered successfully. Check your email to verify your account.";
    }

    //method for existing user to log-in
    public String login(LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByUsername(request.getUsername());
        if(optionalUser.isEmpty()) {
            return "User not found";
        }
        User user = optionalUser.get();
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return "Wrong password";
        }
        if(!user.isVerified()) {
            return "Please verify your email";
        }
        return jwtUtil.generateToken(user.getUsername());
    }

    //used to verify user
    public Optional<User> verifyUser(String token) {
        Optional<User> optionalUser = userRepository.findByVerificationToken(token);
        if(optionalUser.isEmpty()) {
            return Optional.empty();
        }
        User user = optionalUser.get();
        user.setVerified(true);
        user.setVerificationToken(null);
        userRepository.save(user);

        return Optional.of(user);
    }

    //resends verification email
    public void resendVerificationLink(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        if(user.isVerified()) {
            throw new RuntimeException("User already verified");
        }
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        userRepository.save(user);

        String verificationUrl = "http://localhost:8080/api/auth/verify?token=" + token;
        emailService.sendEmail(user.getEmail(), "Verify Your Account", "Click the link to verify your account: " + verificationUrl);
    }

    //checks if email is verified for front-end
    public boolean isEmailVerified(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return user.isVerified();
    }

}

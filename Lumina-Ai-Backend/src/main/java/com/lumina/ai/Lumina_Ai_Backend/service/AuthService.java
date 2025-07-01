package com.lumina.ai.Lumina_Ai_Backend.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.el.stream.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.lumina.ai.Lumina_Ai_Backend.Exception.ResourceNotFoundException;
import com.lumina.ai.Lumina_Ai_Backend.dto.AuthRequest;
import com.lumina.ai.Lumina_Ai_Backend.dto.AuthResponse;
import com.lumina.ai.Lumina_Ai_Backend.entity.Sessions;
import com.lumina.ai.Lumina_Ai_Backend.entity.Users;
import com.lumina.ai.Lumina_Ai_Backend.repo.SessionRepository;
import com.lumina.ai.Lumina_Ai_Backend.repo.UserRepository;
import com.lumina.ai.Lumina_Ai_Backend.util.JwtUtil;

import jakarta.transaction.Transactional;

@Service
public class AuthService {
    
    private final UserRepository repo;
    private final JwtUtil jwtutil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final SessionRepository sessionRepo;



    public AuthService(UserRepository repo, JwtUtil jwtutil, BCryptPasswordEncoder passwordEncoder,SessionRepository sessionRepo){
        this.repo=repo;
        this.jwtutil=jwtutil;
        this.passwordEncoder=passwordEncoder;
        this.sessionRepo=sessionRepo;
      
    }

    @Transactional
    public AuthResponse registerRequest(AuthRequest request){

        String email=request.getEmail().toLowerCase();
        if(repo.findByEmail(email).isPresent()){
            throw new IllegalArgumentException("Email already Exists");
        }
        Users user=new Users();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setLoginType("manual");
        user=repo.save(user);
        return new AuthResponse(null, user.getId().toString());

    }

    @Transactional
    public AuthResponse loginRequest(AuthRequest request){
        String email=request.getEmail().toLowerCase();
        Users user=repo.findByEmail(email)
        .orElseThrow(()->new IllegalArgumentException("Invalid Email or Password"));

        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new IllegalArgumentException("Invalid email or password");
        }

       // Deactivate existing active sessions
        List<Sessions> activeSessions = sessionRepo.findByUserIdAndStatus(user.getId(), Sessions.Status.ACTIVE);
        activeSessions.forEach(session -> {
            session.setStatus(Sessions.Status.INACTIVE);
            sessionRepo.save(session);
        });
        // Create new session
        Sessions session = new Sessions();
        session.setUser(user);
        session.setSessionName("Session_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss")));
        session.setStatus(Sessions.Status.ACTIVE);
        sessionRepo.save(session);
        String jwt = jwtutil.generateToken(user.getId().toString());
        return new AuthResponse(jwt, user.getId().toString());
    }

    @Transactional
    public AuthResponse handleGoogleLogin(OAuth2AuthenticationToken authentication){

        String googleId=authentication.getPrincipal().getAttribute("sub");
        String email=authentication.getPrincipal().getAttribute("email");
        Boolean verifiedEmail= authentication.getPrincipal().getAttribute("verified_email");

        if(email==null || googleId==null){
            throw new IllegalArgumentException("Invalid Google login: Missing or unverified email");
        }
        String normalizedEmail = email.toLowerCase();
        Users user=repo.findByEmail(normalizedEmail)
        .orElseGet(()->{
            Users newUser=new Users();
         newUser.setEmail(normalizedEmail);
         newUser.setGoogleId(googleId);
         newUser.setLoginType("Google_Login");
         return repo.save(newUser);
        });
if (user.getGoogleId() == null) {
        user.setGoogleId(googleId);
        user.setLoginType("Google_Login");
        repo.save(user);
    }
    sessionRepo.findByUserIdAndStatus(user.getId(), Sessions.Status.ACTIVE)
            .forEach(session -> {
                session.setStatus(Sessions.Status.INACTIVE);
                sessionRepo.save(session);
            });

        Sessions session=new Sessions();
        session.setUser(user);   
        session.setSessionName("Google_Login_"+LocalDateTime.now());
        session.setStatus(Sessions.Status.ACTIVE);
        sessionRepo.save(session);



        String jwt = jwtutil.generateToken(user.getId().toString());
        return new AuthResponse(jwt, user.getId().toString());
        
    }

    @Transactional
    public void setPassword(String jwt,String password){
     Long userId = Long.valueOf(jwtutil.extractUserId(jwt));
        String email = jwtutil.extractEmail(jwt).toLowerCase();

        Users user = repo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!user.getId().equals(userId)) {
            throw new IllegalArgumentException("Unauthorized");
        }

        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        user.setPassword(passwordEncoder.encode(password));
        repo.save(user);
    }
}

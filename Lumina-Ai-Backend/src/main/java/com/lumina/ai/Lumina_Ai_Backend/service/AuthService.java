package com.lumina.ai.Lumina_Ai_Backend.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.el.stream.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.lumina.ai.Lumina_Ai_Backend.dto.AuthRequest;
import com.lumina.ai.Lumina_Ai_Backend.dto.AuthResponse;
import com.lumina.ai.Lumina_Ai_Backend.entity.Sessions;
import com.lumina.ai.Lumina_Ai_Backend.entity.Users;
import com.lumina.ai.Lumina_Ai_Backend.repo.SessionRepository;
import com.lumina.ai.Lumina_Ai_Backend.repo.UserRepository;
import com.lumina.ai.Lumina_Ai_Backend.util.JwtUtil;

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

    public AuthResponse registerRequest(AuthRequest request){
        Users user=new Users();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setLoginType("manual");
        user=repo.save(user);
        return new AuthResponse(null, user.getId().toString());

    }


    public AuthResponse loginRequest(AuthRequest request){
        Users user=repo.findByEmail(request.getEmail())
        .orElseThrow(()->new IllegalArgumentException("Invalid Email or Password"));

        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new IllegalArgumentException("Invalid email or password");
        }

        Sessions session=sessionRepo.findByUserIdAndStatus(user.getId(),Sessions.Status.ACTIVE).orElse(null);
        if(session==null){
            session=new Sessions();
            session.setUser(user);
            session.setSessionName("Session_"+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss")));
            session.setStatus(Sessions.Status.ACTIVE);
            sessionRepo.save(session);

        }
        String jwt = jwtutil.generateToken(user.getId().toString());
        return new AuthResponse(jwt, user.getId().toString());
    }

    public AuthResponse handleGoogleLogin(OAuth2AuthenticationToken authentication){

        String googleId=authentication.getPrincipal().getAttribute("sub");
        String email=authentication.getPrincipal().getAttribute("email");

        Users user = repo.findByGoogleId(googleId)
                .orElseGet(() -> {
                    Users newUser = new Users();
                    newUser.setEmail(email);
                    newUser.setGoogleId(googleId);
                    return repo.save(newUser);
                });

        Sessions session=new Sessions();
        session.setUser(user);   
        session.setSessionName("Google_Login_"+LocalDateTime.now());
        session.setStatus(Sessions.Status.ACTIVE);
        sessionRepo.save(session);



        String jwt = jwtutil.generateToken(user.getId().toString());
        return new AuthResponse(jwt, user.getId().toString());



        
    }
}

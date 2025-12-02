package com.sultanmuratyeldar.pastebin.controller.auth;

import com.sultanmuratyeldar.pastebin.entity.Role;
import com.sultanmuratyeldar.pastebin.entity.User;
import com.sultanmuratyeldar.pastebin.repository.RoleRepository;
import com.sultanmuratyeldar.pastebin.repository.UserRepository;
import com.sultanmuratyeldar.pastebin.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request ){
        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(defaultRole)
                .build();

        repository.save(user);


        Set<String> roles = Set.of(user.getRole().getName());

        var jwtToken = jwtService.generateToken(user , roles);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
                  request.getUsername(),
                  request.getPassword()
          )
        );
        var user = repository.findByUsername(request.getUsername()).orElseThrow();

        Set<String> roles = Set.of(user.getRole().getName());

        var jwtToken = jwtService.generateToken(user , roles);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}

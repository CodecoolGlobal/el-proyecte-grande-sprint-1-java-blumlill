package com.codecool.spaceship.auth;

import com.codecool.spaceship.model.UserEntity;
import com.codecool.spaceship.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authenticationService.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authenticationService.authenticate(request);
        System.out.println(response.getToken());
        if(response.getToken() != null) {
            return ResponseEntity.status(201).body(response.toString());
        } else {
            Optional<UserEntity> user = userRepository.findByUsername(request.getUsername());
            if(user == null) {
                return ResponseEntity.status(403).body("invalid username");
            } else {
                return ResponseEntity.status(403).body("invalid password");
            }
        }
    }
}

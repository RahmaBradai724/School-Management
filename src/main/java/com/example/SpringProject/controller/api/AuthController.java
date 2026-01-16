package com.example.SpringProject.controller.api;

import com.example.SpringProject.dto.AuthRequest;
import com.example.SpringProject.dto.AuthResponse;
import com.example.SpringProject.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(new AuthResponse(null, null, null, "Invalid credentials"));
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        final String role = userDetails.getAuthorities().iterator().next().getAuthority();

        return ResponseEntity.ok(new AuthResponse(jwt, userDetails.getUsername(), role, "Login successful"));
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(token, userDetails)) {
                return ResponseEntity.ok("Token is valid");
            } else {
                return ResponseEntity.status(401).body("Token is invalid");
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Token validation failed");
        }
    }
}

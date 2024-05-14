package com.RWI.Nidhi.Security.Controller;


import com.RWI.Nidhi.Security.Jwt.Jwtgen;
import com.RWI.Nidhi.Security.models.Credentials;
import com.RWI.Nidhi.Security.models.ERole;
import com.RWI.Nidhi.Security.models.Role;
import com.RWI.Nidhi.Security.payload.request.LoginRequest;
import com.RWI.Nidhi.Security.payload.request.SignupRequest;
import com.RWI.Nidhi.Security.payload.response.JwtResponse;
import com.RWI.Nidhi.Security.payload.response.MessageResponse;
import com.RWI.Nidhi.Security.repository.CredentialsRepo;
import com.RWI.Nidhi.Security.repository.RoleRepository;
import com.RWI.Nidhi.Security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/home")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    Jwtgen jwtUtils;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Perform authentication
            org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmailOrPhoneNumber(), loginRequest.getPassword()));

            // Set authentication in security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            String jwt = jwtUtils.generateJwtToken(authentication);

            // Get user details from authenticated principal
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Get user roles
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());
            System.out.println(roles.get(0));
            if (roles.get(0).equals("ROLE_USER")) {
                // Return JWT response
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access not granted");

            }
            return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(),
                    userDetails.getEmail(), roles));
        } catch (BadCredentialsException ex) {
            // Handle authentication failure due to bad credentials
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }
//        finally {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//        }
    }
    @PostMapping("/agent")
    public ResponseEntity<?> authenticateAgent(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Perform authentication
            org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmailOrPhoneNumber(), loginRequest.getPassword()));

            // Set authentication in security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            String jwt = jwtUtils.generateJwtToken(authentication);

            // Get user details from authenticated principal
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Get user roles
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());
            if (!roles.get(0).equals("ROLE_AGENT")) {
                // Return JWT response
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access not granted");

            }
            return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(),
                    userDetails.getEmail(), roles));
        } catch (BadCredentialsException ex) {
            // Handle authentication failure due to bad credentials
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");}
//         finally {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//        }
    }
    @PostMapping("/admin")
    public ResponseEntity<?> authenticateAdmin(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Perform authentication
            org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmailOrPhoneNumber(), loginRequest.getPassword()));

            // Set authentication in security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            String jwt = jwtUtils.generateJwtToken(authentication);

            // Get user details from authenticated principal
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Get user roles
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());
            System.out.println(roles.get(0));
            if (!roles.get(0).equals("ROLE_ADMIN")) {
                // Return JWT response
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access not granted");
            }
            return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(),
                    userDetails.getEmail(), roles));
        } catch (BadCredentialsException ex) {
            // Handle authentication failure due to bad credentials
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }
//        finally {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//        }
    }
}

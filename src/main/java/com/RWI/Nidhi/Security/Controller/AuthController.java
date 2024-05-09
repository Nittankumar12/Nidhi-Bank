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
    CredentialsRepo userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    Jwtgen jwtUtils;

//    @PostMapping("/user/signup")
//    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
//        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
//            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
//        }
//
//        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
//            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
//        }
//
//        if (userRepository.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
//            return ResponseEntity.badRequest().body(new MessageResponse("Error: Phone number is already in use!"));
//        }
//
//        // Create new user's account
//        Credentials credentials = new Credentials(signUpRequest.getUsername(), signUpRequest.getEmail(), signUpRequest.getPhoneNumber(),
//                encoder.encode(signUpRequest.getPassword()));
//
//        // Set default role as USER for user
//        Set<Role> roles = new HashSet<>();
//        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//        roles.add(userRole);
//        credentials.setRoles(roles);
//
//        userRepository.save(credentials);
//
//        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
//    }

//    @PostMapping("/agent/signup")
//    public ResponseEntity<?> registerDriver(@Valid @RequestBody SignupRequest signUpRequest) {
//        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
//            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
//        }
//
//        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
//            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
//        }
//
//        if (userRepository.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
//            return ResponseEntity.badRequest().body(new MessageResponse("Error: Phone number is already in use!"));
//        }
//
//        // Create new driver's account
//        Credentials driver = new Credentials(signUpRequest.getUsername(), signUpRequest.getEmail(), signUpRequest.getPhoneNumber(),
//                encoder.encode(signUpRequest.getPassword()));
//
//        // Set default role as agent
//        Set<Role> roles = new HashSet<>();
//        Role driverRole = roleRepository.findByName(ERole.ROLE_AGENT)
//                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//        roles.add(driverRole);
//        driver.setRoles(roles);
//
//        userRepository.save(driver);
//
//        return ResponseEntity.ok(new MessageResponse("Driver registered successfully!"));
//    }

//    @PostMapping("/hub/signup")
//    public ResponseEntity<?> registerHub(@Valid @RequestBody SignupRequest signUpRequest) {
//        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
//            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
//        }
//
//        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
//            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
//        }
//
//
//        // Create new hub's account
//        User hub = new User();
//
//        // Set email and password for hub
//        hub.setUsername(signUpRequest.getUsername());
//        hub.setEmail(signUpRequest.getEmail());
//        hub.setPassword(encoder.encode(signUpRequest.getPassword()));
//
//        // Set designation for hub
//        hub.setDesignation(signUpRequest.getDesignation());
//
//        userRepository.save(hub);
//
//        return ResponseEntity.ok(new MessageResponse("Hub registered successfully!"));
//    }


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
            // Return JWT response
            return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(),
                    userDetails.getEmail(), roles));
        } catch (BadCredentialsException ex) {
            // Handle authentication failure due to bad credentials
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }
    }
}

package com.kbc.service;

import com.kbc.dto.LoginRequest;
import com.kbc.dto.SignupRequest;
import com.kbc.exception.CustomException;
import com.kbc.model.ERole;
import com.kbc.model.PasswordResetToken;
import com.kbc.model.Role;
import com.kbc.model.User;
import com.kbc.repository.RoleRepository;
import com.kbc.repository.UserRepository;
import com.kbc.security.JwtUtils;
import com.kbc.security.UserDetailsImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.kbc.repository.PasswordResetTokenRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final EmailService emailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public AuthService(AuthenticationManager authenticationManager, 
                      UserRepository userRepository, 
                      RoleRepository roleRepository, 
                      PasswordEncoder passwordEncoder, 
                      JwtUtils jwtUtils,
                      EmailService emailService,
                      PasswordResetTokenRepository passwordResetTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.emailService = emailService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    public String authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtUtils.generateJwtToken(authentication);
    }

    public void registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new CustomException("Error: Email is already in use!");
        }
    
        // Create new user's account
        User user = new User(signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                signUpRequest.getFirstName(),
                signUpRequest.getLastName());
    
        // Set passwordChanged to true since user created their own password
        user.setPasswordChanged(true);
    
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new CustomException("Error: Role is not found."));
        roles.add(userRole);
        user.setRoles(roles);
        
        userRepository.save(user);
    }

    public void createUserByAdmin(String email, Set<String> strRoles) {
        if (userRepository.existsByEmail(email)) {
            throw new CustomException("Error: Email is already in use!");
        }

        // Generate random password
        String rawPassword = generateRandomPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);

        User user = new User(email, encodedPassword);
        
        Set<Role> roles = new HashSet<>();
        if (strRoles == null || strRoles.isEmpty()) {
            // If no roles specified, assign USER role by default
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new CustomException("Error: Default role not found."));
            roles.add(userRole);
        } else {
            // Process each requested role
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new CustomException("Error: Admin role not found."));
                        roles.add(adminRole);
                        break;
                    case "parent":
                        Role parentRole = roleRepository.findByName(ERole.ROLE_PARENT)
                                .orElseThrow(() -> new CustomException("Error: Parent role not found."));
                        roles.add(parentRole);
                        break;
                    case "head_of_finance":
                        Role financeRole = roleRepository.findByName(ERole.ROLE_HEAD_OF_FINANCE)
                                .orElseThrow(() -> new CustomException("Error: Head of Finance role not found."));
                        roles.add(financeRole);
                        break;
                    case "user":
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new CustomException("Error: User role not found."));
                        roles.add(userRole);
                        break;
                }
            });
        }
        
        user.setRoles(roles);
        userRepository.save(user);
        
        // Send email with credentials
        emailService.sendUserCreationEmail(email, rawPassword);
    }

    private String generateRandomPassword() {
        // Implement a proper random password generator
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public void changePassword(UserDetailsImpl userDetails, String newPassword) {
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new CustomException("User not found"));
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordChanged(true);
        userRepository.save(user);
    }
    
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found with this email"));
        
        // Delete existing token if any
        passwordResetTokenRepository.findByUser(user).ifPresent(token -> 
            passwordResetTokenRepository.delete(token));
        
        // Create new token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(resetToken);
        
        // Send email
        String resetLink = "http://localhost:3000/reset-password/" + token;
        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new CustomException("Invalid token"));
        
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new CustomException("Token expired");
        }
        
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordChanged(true);
        userRepository.save(user);
        
        // Delete the used token
        passwordResetTokenRepository.delete(resetToken);
    }
    
    /**
     * Deletes a user by their ID
     * Only accessible by admin users
     * @param userId the UUID of the user to delete
     */
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found with id: " + userId));
        
        // First, delete any password reset tokens associated with this user
        passwordResetTokenRepository.findByUser(user).ifPresent(token -> 
            passwordResetTokenRepository.delete(token));
            
        // Then delete the user
        userRepository.delete(user);
    }
}
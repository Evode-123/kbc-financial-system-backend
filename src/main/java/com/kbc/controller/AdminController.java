package com.kbc.controller;

import com.kbc.dto.UserDto;
import com.kbc.service.AuthService;
import com.kbc.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "https://kingdom-believers-church.vercel.app")
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserService userService;
    private final AuthService authService;
    
    public AdminController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }
    
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
    
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody Map<String, Object> requestBody) {
        String email = (String) requestBody.get("email");
        
        // Convert the ArrayList to a Set instead of casting
        @SuppressWarnings("unchecked")
        List<String> rolesList = (List<String>) requestBody.get("roles");
        Set<String> roles = new HashSet<>(rolesList);
        
        authService.createUserByAdmin(email, roles);
        return ResponseEntity.ok("User created successfully");
    }
    
    @PutMapping("/{id}/roles")
    public ResponseEntity<?> updateUserRoles(@PathVariable UUID id,
                                        @RequestBody Set<String> roles) {
        userService.updateUserRoles(id, roles);
        return ResponseEntity.ok("User roles updated successfully");
    }
    
    /**
     * Endpoint to delete a user by their ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        authService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
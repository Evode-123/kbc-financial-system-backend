package com.kbc.service;

import com.kbc.dto.UserDto;
import com.kbc.exception.CustomException;
import com.kbc.model.ERole;
import com.kbc.model.Role;
import com.kbc.model.User;
import com.kbc.repository.RoleRepository;
import com.kbc.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException("User not found"));
        return convertToDto(user);
    }

    public void updateUserRoles(UUID userId, Set<String> strRoles) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found"));
                
        Set<Role> roles = new HashSet<>();
        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new CustomException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new CustomException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "parent":
                        Role parentRole = roleRepository.findByName(ERole.ROLE_PARENT)
                                .orElseThrow(() -> new CustomException("Error: Role is not found."));
                        roles.add(parentRole);
                        break;
                    case "head_of_finance":
                        Role financeRole = roleRepository.findByName(ERole.ROLE_HEAD_OF_FINANCE)
                                .orElseThrow(() -> new CustomException("Error: Role is not found."));
                        roles.add(financeRole);
                        break;
                    case "user":
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new CustomException("Error: Role is not found."));
                        roles.add(userRole);
                        break;
                }
            });
        }
                
        user.setRoles(roles);
        userRepository.save(user);
    }

    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPasswordChanged(user.isPasswordChanged());
        dto.setRoles(user.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet()));
        return dto;
    }
}
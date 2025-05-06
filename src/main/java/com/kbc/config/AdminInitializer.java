package com.kbc.config;

import com.kbc.model.ERole;
import com.kbc.model.Role;
import com.kbc.model.User;
import com.kbc.repository.RoleRepository;
import com.kbc.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AdminInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(UserRepository userRepository, 
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            // Create roles using proper constructor
            Role adminRole = new Role();
            adminRole.setName(ERole.ROLE_ADMIN);
            
            Role userRole = new Role();
            userRole.setName(ERole.ROLE_USER);

            Role parentRole = new Role();
            parentRole.setName(ERole.ROLE_PARENT);

            Role headOfFinanceRole = new Role();
            headOfFinanceRole.setName(ERole.ROLE_HEAD_OF_FINANCE);
            
            roleRepository.save(adminRole);
            roleRepository.save(userRole);
            roleRepository.save(parentRole);
            roleRepository.save(headOfFinanceRole);
            
            // Create admin user
            User admin = new User();
            admin.setEmail("evodeniyitegeka10@gmail.com");
            admin.setPassword(passwordEncoder.encode("evode2002@"));
            admin.setPasswordChanged(false);
            admin.setRoles(Set.of(adminRole));
            
            userRepository.save(admin);
        }
    }
}
package ru.kata.spring.boot_security.demo.init;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class Init {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Init(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @PostConstruct
    public void postConstruct(){
        Role adminRole = new Role();
        adminRole.setId(1L);
        adminRole.setRole("ROLE_ADMIN");
        Role userRole = new Role();
        userRole.setId(2L);
        userRole.setRole("ROLE_USER");
        roleRepository.saveAll(List.of(adminRole, userRole));

        User admin = new User();
        admin.setName("admin");
        admin.setLastName("admin");
        admin.setAge(27);
        admin.setEmail("admin@mail.com");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.addRole(adminRole);

        userRepository.save(admin);
    }
}

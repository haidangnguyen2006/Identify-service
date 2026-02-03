/* (C)2025 */
package com.bill.identity_service.configuration;

import com.bill.identity_service.entity.Role;
import com.bill.identity_service.entity.User;
import com.bill.identity_service.enums.RoleEnum;
import com.bill.identity_service.repository.RoleRepository;
import com.bill.identity_service.repository.UserRepository;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "org.postgresql.Driver")
    ApplicationRunner applicationRunner(
            UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                Set<Role> roles = new HashSet<Role>();
                roles.add(
                        roleRepository
                                .findById(RoleEnum.ADMIN.name())
                                .orElseGet(
                                        () -> {
                                            Role role =
                                                    Role.builder()
                                                            .name("ADMIN")
                                                            .description("Administrator role")
                                                            .build();
                                            roleRepository.save(role);
                                            return role;
                                        }));
                User user =
                        User.builder()
                                .username("admin")
                                .firstname("admin")
                                .lastname("admin")
                                .password(passwordEncoder.encode("admin"))
                                .roles(roles)
                                .build();
                userRepository.save(user);
                log.warn("Admin user has been created with default password, please change it!");
            }
        };
    }
}

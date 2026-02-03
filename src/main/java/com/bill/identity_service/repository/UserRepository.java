/* (C)2025 */
package com.bill.identity_service.repository;

import com.bill.identity_service.entity.Role;
import com.bill.identity_service.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    List<User> findByRolesContaining(Role role);
}

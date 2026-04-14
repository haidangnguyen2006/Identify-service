/* (C)2025 */
package com.bill.identity_service.repository;

import com.bill.identity_service.entity.Role;
import com.bill.identity_service.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    //@EntityGraph(attributePaths = {"roles", "roles.permissions"})
    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN FETCH u.roles r " +
            "LEFT JOIN FETCH r.permissions")
    List<User> findAll();

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    List<User> findByRolesContaining(Role role);
}

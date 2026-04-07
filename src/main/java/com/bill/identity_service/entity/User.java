/* (C)2025 */
package com.bill.identity_service.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data // Auto generate setter & getter
@NoArgsConstructor // generate constructor with no Args
@AllArgsConstructor // generate constrctor with all Args
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name="username",unique = true)
    String username;
    String password;
    String firstname;
    String lastname;
    LocalDate dob;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_name")
    )
    Set<Role> roles;
}

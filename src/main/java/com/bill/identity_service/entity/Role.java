/* (C)2025 */
package com.bill.identity_service.entity;

import jakarta.persistence.*;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data // Auto generate setter & getter
@NoArgsConstructor // generate constructor with no Args
@AllArgsConstructor // generate constrctor with all Args
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Role")
public class Role {
    @Id String name;
    String description;

    @EqualsAndHashCode.Exclude // Loại bỏ field này khỏi hashCode
    @ToString.Exclude
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Thêm cascade
    @JoinTable(
            name = "role_permissions", // Tên bảng trung gian của bạn
            joinColumns = @JoinColumn(name = "role_name"), // Tên cột tham chiếu đến Role
            inverseJoinColumns =
                    @JoinColumn(name = "permissions_name") // Tên cột tham chiếu đến Permission
            )
    Set<Permission> permissions;
}

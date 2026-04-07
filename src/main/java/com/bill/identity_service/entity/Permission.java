/* (C)2025 */
package com.bill.identity_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data // Auto generate setter & getter
@NoArgsConstructor // generate constructor with no Args
@AllArgsConstructor // generate constrctor with all Args
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Permission")
public class Permission extends BaseEntity {
    @Id String name;

    String description;
}

/* (C)2025 */
package com.bill.identity_service.dto.request;

import com.bill.identity_service.validator.DobConstraint;
import java.time.LocalDate;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {

    // @Size(min =8, message= "PASSWORD_INVALID")
    String password;
    String firstname;
    String lastname;

    @DobConstraint(min = 16, message = "DOB_INVALID")
    LocalDate dob;

    List<String> roles;
}

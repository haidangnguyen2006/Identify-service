/* (C)2025 */
package com.bill.identity_service.dto.request;

import com.bill.identity_service.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data // Auto generate setter & getter
@NoArgsConstructor // generate constructor with no Args
@AllArgsConstructor // generate constrctor with all Args
@Builder
@FieldDefaults(
        level = AccessLevel.PRIVATE) // Auto set special modified to private if not define special
// modified for field
public class UserCreationRequest {
    @Size(min = 3, message = "USERNAME_INVALID")
    String username;

    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;

    String firstname;
    String lastname;

    @DobConstraint(min = 16, message = "DOB_INVALID")
    LocalDate dob;
}

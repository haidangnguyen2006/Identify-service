/* (C)2025 */
package com.bill.identity_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data // Auto generate setter & getter
@NoArgsConstructor // generate constructor with no Args
@AllArgsConstructor // generate constrctor with all Args
@Builder
@FieldDefaults(
        level = AccessLevel.PRIVATE) // Auto set special modified to private if not define special
// modified for field
public class AuthenticationRequest {
    String username;
    String password;
}

/* (C)2025 */
package com.bill.identity_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    USER_EXISTED(666, "User existed!!", HttpStatus.BAD_REQUEST),
    USER_UN_EXISTED(777, "User not existed!!", HttpStatus.NOT_FOUND),
    UNCATEGORIZED(999, "Uncategorized Exception!!", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(304, "Uncategorized Exception!!", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(103, "Username must be at least {min} characters!!", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(104, "Password must be at least {min} characters!", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATION(100, "Your account cannot authenticate", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(101, "Your account do not have permission", HttpStatus.FORBIDDEN),
    CANNOT_CREATE_TOKEN(106, "Can not create your token", HttpStatus.BAD_REQUEST),
    PERMISSION_UN_EXISTED(1010, "Permission not existed!!", HttpStatus.NOT_FOUND),
    ROLE_UN_EXISTED(2020, "Role not existed!!", HttpStatus.NOT_FOUND),
    ROLE_EXISTED(2021, "Role existed!!", HttpStatus.BAD_REQUEST),
    ROLE_IN_USE(1055, "Role is in use, cannot delete!!", HttpStatus.BAD_REQUEST),
    DOB_INVALID(105, "User must be at least {min} years old!!", HttpStatus.BAD_REQUEST);
    private int code;
    private String message;
    private HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}

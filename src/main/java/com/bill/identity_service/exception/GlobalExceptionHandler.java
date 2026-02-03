/* (C)2025 */
package com.bill.identity_service.exception;

import com.bill.identity_service.dto.request.ApiRespone;
import jakarta.validation.ConstraintViolation;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiRespone> handlingException(Exception exception) {
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED;
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(
                        ApiRespone.builder()
                                .code(errorCode.getCode())
                                .result(errorCode.getMessage())
                                .build());
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiRespone> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(
                        ApiRespone.builder()
                                .result(errorCode.getMessage())
                                .code(errorCode.getCode())
                                .build());
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiRespone> handlingAccessDeniedException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(
                        ApiRespone.builder()
                                .code(errorCode.getCode())
                                .result(errorCode.getMessage())
                                .build());
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    ResponseEntity<String> handlingDataIntegrityViolationException(
            DataIntegrityViolationException exception) {
        return ResponseEntity.badRequest().body(exception.getMostSpecificCause().getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiRespone> handlingMeMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        String enumkey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String, Object> attributes = Map.of();
        try {
            errorCode = ErrorCode.valueOf(enumkey);
            var constraintViolation =
                    exception
                            .getBindingResult()
                            .getAllErrors()
                            .getFirst()
                            .unwrap(ConstraintViolation.class);
            attributes = constraintViolation.getConstraintDescriptor().getAttributes();
            log.warn("Validation attributes: {}", attributes);
        } catch (IllegalArgumentException e) {

        }
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(
                        ApiRespone.builder()
                                .code(errorCode.getCode())
                                .message(
                                        Objects.nonNull(attributes)
                                                ? mapAtributes(attributes, errorCode.getMessage())
                                                : errorCode.getMessage())
                                .build());
    }

    private String mapAtributes(Map<String, Object> attributes, String message) {
        String min = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        return message.replace("{" + MIN_ATTRIBUTE + "}", min);
    }
}

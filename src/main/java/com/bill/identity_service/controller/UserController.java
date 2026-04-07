/* (C)2025 */
package com.bill.identity_service.controller;

import com.bill.identity_service.dto.request.ApiRespone;
import com.bill.identity_service.dto.request.UserCreationRequest;
import com.bill.identity_service.dto.request.UserUpdateRequest;
import com.bill.identity_service.dto.response.UserResponse;
import com.bill.identity_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "User Controller", description = "These API are provided to manage user") // Tên nhóm API
public class UserController {
    UserService userService;

    @Operation(summary = "Create a new user", description = "This api is used to create a new user in the system")
    @PostMapping
    ApiRespone<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiRespone<UserResponse> apiRespone = new ApiRespone<>();
        apiRespone.setResult(userService.createUser(request));

        return apiRespone;
    }

    @GetMapping
    ApiRespone<List<UserResponse>> getUsers() {

        return ApiRespone.<List<UserResponse>>builder().result(userService.getUsers()).build();
    }

    @GetMapping("/{userId}")
    UserResponse getUserByID(@PathVariable("userId") String userId) {

        return userService.getUserById(userId);
    }

    @GetMapping("/myInfo")
    UserResponse getMyInfo() {

        return userService.getMyInfo();
    }

    @PutMapping("/{userId}")
    UserResponse updateUser(
            @PathVariable String userId, @RequestBody @Valid UserUpdateRequest request) {
        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/{userId}")
    ApiRespone<Void> deleteUser(@PathVariable String userId) {

        userService.deleteUser(userId);
        return ApiRespone.<Void>builder().build();
    }
}

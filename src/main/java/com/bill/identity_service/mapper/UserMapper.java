/* (C)2025 */
package com.bill.identity_service.mapper;

import com.bill.identity_service.dto.request.ApiRespone;
import com.bill.identity_service.dto.request.UserCreationRequest;
import com.bill.identity_service.dto.request.UserUpdateRequest;
import com.bill.identity_service.dto.response.UserResponse;
import com.bill.identity_service.entity.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    List<UserResponse> toUserResponse(List<User> users);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    // Helper method để tạo ApiResponse
    default ApiRespone<UserResponse> toApiResponse(User user) {
        return ApiRespone.<UserResponse>builder()
                .message("Success")
                .result(toUserResponse(user))
                .build();
    }

    default ApiRespone<List<UserResponse>> toApiResponseList(List<User> users) {
        return ApiRespone.<List<UserResponse>>builder()
                .message("Success")
                .result(toUserResponse(users))
                .build();
    }
}

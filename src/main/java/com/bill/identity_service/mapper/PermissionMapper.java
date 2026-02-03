/* (C)2025 */
package com.bill.identity_service.mapper;

import com.bill.identity_service.dto.request.PermissionRequest;
import com.bill.identity_service.dto.request.PermissionUpdateRequest;
import com.bill.identity_service.dto.response.PermissionRespone;
import com.bill.identity_service.entity.Permission;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionRespone toPermissionRespone(Permission permission);

    List<PermissionRespone> toPermissionRespone(List<Permission> permissions);

    void updatePermission(@MappingTarget Permission permission, PermissionUpdateRequest request);
    // Helper method để tạo ApiResponse
    /*default ApiRespone<PermissionRespone> toApiResponse(Permission permissions) {
        return ApiRespone.<PermissionRespone>builder()
                .message("Success")
                .result(toPermissionRespone(permissions))
                .build();
    }

    default ApiRespone<List<PermissionRespone>> toApiResponseList(List<Permission> permissions) {
        return ApiRespone.<List<PermissionRespone>>builder()
                .message("Success")
                .result(toPermissionRespone(permissions))
                .build();
    }*/
}

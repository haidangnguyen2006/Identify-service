/* (C)2025 */
package com.bill.identity_service.mapper;

import com.bill.identity_service.dto.request.RoleRequest;
import com.bill.identity_service.dto.request.RoleUpdateRequest;
import com.bill.identity_service.dto.response.RoleRespone;
import com.bill.identity_service.entity.Role;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleRespone toRoleRespone(Role role);

    List<RoleRespone> toRoleRespone(List<Role> roles);

    void updateRole(@MappingTarget Role role, RoleUpdateRequest request);
}

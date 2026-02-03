/* (C)2025 */
package com.bill.identity_service.service;

import com.bill.identity_service.dto.request.RoleRequest;
import com.bill.identity_service.dto.request.RoleUpdateRequest;
import com.bill.identity_service.dto.response.RoleRespone;
import com.bill.identity_service.entity.Role;
import com.bill.identity_service.entity.User;
import com.bill.identity_service.exception.AppException;
import com.bill.identity_service.exception.ErrorCode;
import com.bill.identity_service.mapper.RoleMapper;
import com.bill.identity_service.repository.PermissionRepository;
import com.bill.identity_service.repository.RoleRepository;
import com.bill.identity_service.repository.UserRepository;
import java.util.HashSet;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    UserRepository userRepository;
    PermissionRepository permissionRepository;

    public RoleRespone create(RoleRequest request) {
        if (roleRepository.existsById(request.getName())) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }
        Role role = roleMapper.toRole(request);
        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        roleRepository.save(role);
        return roleMapper.toRoleRespone(role);
    }

    public RoleRespone update(
            String name, @org.jetbrains.annotations.NotNull RoleUpdateRequest request) {
        var role =
                roleRepository
                        .findById(name)
                        .orElseThrow(() -> new AppException(ErrorCode.ROLE_UN_EXISTED));
        roleMapper.updateRole(role, request);

        return roleMapper.toRoleRespone(roleRepository.save(role));
    }

    public RoleRespone getByName(String name) {
        var role =
                roleRepository
                        .findById(name)
                        .orElseThrow(() -> new AppException(ErrorCode.ROLE_UN_EXISTED));

        return roleMapper.toRoleRespone(role);
    }

    public List<RoleRespone> getAll() {
        List<Role> roles = roleRepository.findAll();
        return roleMapper.toRoleRespone(roles);
    }

    public void delete(String name) {
        log.warn("Vao duoc ham delete");
        var role =
                roleRepository
                        .findById(name)
                        .orElseThrow(() -> new AppException(ErrorCode.ROLE_UN_EXISTED));
        log.warn("Role tim duoc: {}", role.getName());
        List<User> users = userRepository.findByRolesContaining(role);
        log.warn("So nguoi dung su dung role: {}", users.size());
        if (!users.isEmpty()) {
            throw new AppException(ErrorCode.ROLE_IN_USE);
        }
        roleRepository.delete(role);
    }
}

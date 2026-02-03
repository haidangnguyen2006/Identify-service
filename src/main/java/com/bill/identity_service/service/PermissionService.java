/* (C)2025 */
package com.bill.identity_service.service;

import com.bill.identity_service.dto.request.PermissionRequest;
import com.bill.identity_service.dto.request.PermissionUpdateRequest;
import com.bill.identity_service.dto.response.PermissionRespone;
import com.bill.identity_service.entity.Permission;
import com.bill.identity_service.exception.AppException;
import com.bill.identity_service.exception.ErrorCode;
import com.bill.identity_service.mapper.PermissionMapper;
import com.bill.identity_service.repository.PermissionRepository;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionRespone create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        log.warn(permission.getName() + "-" + permission.getDescription());
        permissionRepository.save(permission);
        return permissionMapper.toPermissionRespone(permission);
    }

    public List<PermissionRespone> getAll() {
        return permissionMapper.toPermissionRespone(permissionRepository.findAll());
    }

    public void delete(String permission) {
        permissionRepository.deleteById(permission);
    }

    public PermissionRespone update(
            String name, @org.jetbrains.annotations.NotNull PermissionUpdateRequest request) {
        var permission =
                permissionRepository
                        .findById(name)
                        .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_UN_EXISTED));
        permissionMapper.updatePermission(permission, request);

        return permissionMapper.toPermissionRespone(permissionRepository.save(permission));
    }
}

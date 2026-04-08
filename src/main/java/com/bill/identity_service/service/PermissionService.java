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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    // 1. TẠO MỚI: Xóa toàn bộ cache cũ đi (vì danh sách đã bị thay đổi)
    // allEntries = true nghĩa là xóa sạch mọi key nằm trong thư mục "permissions" của Redis
    @CacheEvict(value="permissions",allEntries = true)
    public PermissionRespone create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        log.warn(permission.getName() + "-" + permission.getDescription());
        permissionRepository.save(permission);
        return permissionMapper.toPermissionRespone(permission);
    }

    @Cacheable(value = "permissions", key="'all'")
    public List<PermissionRespone> getAll() {
        log.info("🔴 Đang truy vấn Database để lấy danh sách Permissions...");
        return permissionMapper.toPermissionRespone(permissionRepository.findAll());
    }

    @CacheEvict(value = "permissions", allEntries = true)
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

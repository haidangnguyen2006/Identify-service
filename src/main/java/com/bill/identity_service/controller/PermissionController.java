/* (C)2025 */
package com.bill.identity_service.controller;

import com.bill.identity_service.dto.request.ApiRespone;
import com.bill.identity_service.dto.request.PermissionRequest;
import com.bill.identity_service.dto.request.PermissionUpdateRequest;
import com.bill.identity_service.dto.response.PermissionRespone;
import com.bill.identity_service.service.PermissionService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    ApiRespone<PermissionRespone> create(@RequestBody PermissionRequest request) {
        return ApiRespone.<PermissionRespone>builder()
                .result(permissionService.create(request))
                .build();
    }

    @GetMapping
    ApiRespone<List<PermissionRespone>> getPermissions() {
        return ApiRespone.<List<PermissionRespone>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @DeleteMapping("/{permissionId}")
    ApiRespone<Void> delete(@PathVariable String permissionId) {
        permissionService.delete(permissionId);
        return ApiRespone.<Void>builder().build();
    }

    @PutMapping("/{permissionId}")
    ApiRespone<PermissionRespone> update(
            @PathVariable String permissionId, @RequestBody PermissionUpdateRequest description) {
        return ApiRespone.<PermissionRespone>builder()
                .result(permissionService.update(permissionId, description))
                .build();
    }
}

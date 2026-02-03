/* (C)2025 */
package com.bill.identity_service.controller;

import com.bill.identity_service.dto.request.ApiRespone;
import com.bill.identity_service.dto.request.RoleRequest;
import com.bill.identity_service.dto.request.RoleUpdateRequest;
import com.bill.identity_service.dto.response.RoleRespone;
import com.bill.identity_service.service.RoleService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping
    ApiRespone<RoleRespone> create(@RequestBody RoleRequest request) {
        return ApiRespone.<RoleRespone>builder().result(roleService.create(request)).build();
    }

    @GetMapping("/{roleName}")
    ApiRespone<RoleRespone> getByName(@PathVariable String name) {
        return ApiRespone.<RoleRespone>builder().result(roleService.getByName(name)).build();
    }

    @GetMapping
    ApiRespone<List<RoleRespone>> getRoles() {
        return ApiRespone.<List<RoleRespone>>builder().result(roleService.getAll()).build();
    }

    @DeleteMapping("/{roleName}")
    ApiRespone<Void> delete(@PathVariable String roleName) {
        roleService.delete(roleName);
        return ApiRespone.<Void>builder().build();
    }

    @PutMapping("/{roleName}")
    ApiRespone<RoleRespone> update(
            @PathVariable String roleName, @RequestBody RoleUpdateRequest request) {
        return ApiRespone.<RoleRespone>builder()
                .result(roleService.update(roleName, request))
                .build();
    }
}

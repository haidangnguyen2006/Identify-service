/* (C)2025 */
package com.bill.identity_service.service;

import com.bill.identity_service.constant.PredefinedRole;
import com.bill.identity_service.dto.request.UserCreationRequest;
import com.bill.identity_service.dto.request.UserUpdateRequest;
import com.bill.identity_service.dto.response.UserResponse;
import com.bill.identity_service.entity.Role;
import com.bill.identity_service.entity.User;
import com.bill.identity_service.enums.RoleEnum;
import com.bill.identity_service.exception.AppException;
import com.bill.identity_service.exception.ErrorCode;
import com.bill.identity_service.mapper.UserMapper;
import com.bill.identity_service.repository.RoleRepository;
import com.bill.identity_service.repository.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    public UserResponse createUser(UserCreationRequest request) {

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);
        user.setRoles(roles);
        try{
            user=userRepository.save(user);

        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        return userMapper.toUserResponse(userRepository.findAll());
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUserById(String id) {
        return userMapper.toUserResponse(
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_UN_EXISTED)));
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        User user =
                userRepository
                        .findByUsername(context.getAuthentication().getName())
                        .orElseThrow(() -> new AppException(ErrorCode.USER_UN_EXISTED));
        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(
            String id, @org.jetbrains.annotations.NotNull UserUpdateRequest request) {
        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_UN_EXISTED));
        userMapper.updateUser(user, request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String id) {
        userRepository.delete(
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_UN_EXISTED)));
    }
}

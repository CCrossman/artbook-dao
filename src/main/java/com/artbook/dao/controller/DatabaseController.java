package com.artbook.dao.controller;

import com.artbook.dao.domain.PermissionDTO;
import com.artbook.dao.domain.RoleDTO;
import com.artbook.dao.domain.UserDTO;
import com.artbook.dao.entity.PermissionEntity;
import com.artbook.dao.entity.RoleEntity;
import com.artbook.dao.entity.UserEntity;
import com.artbook.dao.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/database")
public class DatabaseController {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseController.class);

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user")
    public UserDTO getUser(@RequestParam(name = "id", required = false) Long id, @RequestParam(name = "email", required = false) String email) {
        logger.info("getUser: id={}, email={}", id, email);

        if (id != null) {
            return userRepository.findById(id).map(DatabaseController::toDTO).orElse(null);
        }
        if (email != null && !email.isEmpty()) {
            return userRepository.findByEmail(email).map(DatabaseController::toDTO).orElse(null);
        }
        logger.warn("Must provided either id or email to find a user.");
        return null;
    }

    private static UserDTO toDTO(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        return UserDTO.builder()
            .id(entity.getId())
            .email(entity.getEmail())
            .passwordHash(entity.getPasswordHash())
            .role(toDTO(entity.getRole()))
            .deleted(entity.getIsDeleted())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    private static RoleDTO toDTO(RoleEntity entity) {
        if (entity == null) {
            return null;
        }
        return RoleDTO.builder()
            .id(entity.getId())
            .name(entity.getName())
            .permissions(toDTO(entity.getPermissions()))
            .createdAt(entity.getCreatedAt())
            .build();
    }

    private static List<PermissionDTO> toDTO(Collection<PermissionEntity> permissions) {
        if (permissions == null) {
            return null;
        }
        return permissions.stream()
            .map(DatabaseController::toDTO)
            .filter(Objects::nonNull)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    private static PermissionDTO toDTO(PermissionEntity entity) {
        if (entity == null) {
            return null;
        }
        return PermissionDTO.builder()
            .id(entity.getId())
            .name(entity.getName())
            .createdAt(entity.getCreatedAt())
            .build();
    }
}

package com.artbook.dao.service;

import com.artbook.dao.domain.RoleDTO;
import com.artbook.dao.domain.UserDTO;
import com.artbook.dao.entity.RoleEntity;
import com.artbook.dao.entity.UserEntity;
import com.artbook.dao.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserConverter implements Converter<UserEntity, UserDTO> {

    @Autowired
    private Converter<RoleEntity,RoleDTO> roleConverter;

    @Override
    public UserDTO convert(UserEntity entity) throws Exception {
        if (entity == null) {
            return null;
        }
        return UserDTO.builder()
            .id(entity.getId())
            .email(entity.getEmail())
            .passwordHash(entity.getPasswordHash())
            .role(roleConverter.convert(entity.getRole()))
            .deleted(entity.getIsDeleted())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}

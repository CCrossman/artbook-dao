package com.artbook.dao.service;

import com.artbook.dao.domain.PermissionDTO;
import com.artbook.dao.domain.RoleDTO;
import com.artbook.dao.entity.PermissionEntity;
import com.artbook.dao.entity.RoleEntity;
import com.artbook.dao.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class DefaultRoleConverter implements Converter<RoleEntity, RoleDTO> {

    @Autowired
    private Converter<Collection<PermissionEntity>,List<PermissionDTO>> permissionsConverter;

    @Override
    public RoleDTO convert(RoleEntity entity) throws Exception {
        if (entity == null) {
            return null;
        }
        return RoleDTO.builder()
            .id(entity.getId())
            .name(entity.getName())
            .permissions(permissionsConverter.convert(entity.getPermissions()))
            .createdAt(entity.getCreatedAt())
            .build();
    }
}

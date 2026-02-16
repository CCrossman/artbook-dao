package com.artbook.dao.service;

import com.artbook.dao.domain.PermissionDTO;
import com.artbook.dao.entity.PermissionEntity;
import com.artbook.dao.util.Converter;
import org.springframework.stereotype.Component;

@Component
public class DefaultPermissionConverter implements Converter<PermissionEntity, PermissionDTO> {

    @Override
    public PermissionDTO convert(PermissionEntity entity) throws Exception {
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

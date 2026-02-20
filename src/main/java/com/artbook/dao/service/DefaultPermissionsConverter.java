package com.artbook.dao.service;

import com.artbook.dao.domain.PermissionDTO;
import com.artbook.dao.entity.PermissionEntity;
import com.artbook.dao.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class DefaultPermissionsConverter implements Converter<Collection<PermissionEntity>, List<PermissionDTO>> {

    @Autowired
    private Converter<PermissionEntity,PermissionDTO> permissionConverter;

    @Override
    public List<PermissionDTO> convert(Collection<PermissionEntity> permissions) throws Exception {
        if (permissions == null) {
            return null;
        }
        return permissions.stream()
            .map(p -> permissionConverter.convertUnchecked(p))
            .filter(Objects::nonNull)
            .collect(Collectors.toCollection(ArrayList::new));
    }
}

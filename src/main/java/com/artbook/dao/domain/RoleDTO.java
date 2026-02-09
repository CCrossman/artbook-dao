package com.artbook.dao.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

import static com.artbook.dao.util.Preconditions.requireNonEmpty;
import static java.util.Objects.requireNonNull;

@Builder
@EqualsAndHashCode
@Getter
@ToString
public class RoleDTO {
    private final long id;
    private final String name;
    private final ZonedDateTime createdAt;
    private final List<PermissionDTO> permissions;

    public RoleDTO(long id, String name, ZonedDateTime createdAt, Collection<PermissionDTO> permissions) {
        this.id = id;
        this.name = requireNonEmpty(name);
        this.createdAt = requireNonNull(createdAt);
        this.permissions = List.copyOf(permissions);
    }
}

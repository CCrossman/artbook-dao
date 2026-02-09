package com.artbook.dao.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.ZonedDateTime;

import static com.artbook.dao.util.Preconditions.requireNonEmpty;
import static java.util.Objects.requireNonNull;

@Builder
@EqualsAndHashCode
@Getter
@ToString
public class PermissionDTO {
    private final long id;
    private final String name;
    private final ZonedDateTime createdAt;

    public PermissionDTO(long id, String name, ZonedDateTime createdAt) {
        this.id = id;
        this.name = requireNonEmpty(name);
        this.createdAt = requireNonNull(createdAt);
    }
}

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
public class UserDTO {
    private final long id;
    private final String email;
    private final String passwordHash;
    private final boolean deleted;
    private final RoleDTO role;
    private final ZonedDateTime createdAt;
    private final ZonedDateTime updatedAt;

    public UserDTO(long id, String email, String passwordHash, boolean deleted, RoleDTO role, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
        this.id = id;
        this.email = requireNonEmpty(email);
        this.passwordHash = requireNonEmpty(passwordHash);
        this.deleted = deleted;
        this.role = requireNonNull(role);
        this.createdAt = requireNonNull(createdAt);
        this.updatedAt = requireNonNull(updatedAt);
    }
}
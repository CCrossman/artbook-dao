package com.artbook.dao.domain;

import java.util.List;

import static java.util.Objects.requireNonNull;

public record Page<T>(List<T> items, int pageNo, int pageSize, int totalCount) {
    public Page {
        requireNonNull(items, "items cannot be null");
    }
}

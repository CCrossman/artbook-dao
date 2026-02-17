package com.artbook.dao.domain;

import java.util.Map;
import java.util.UUID;

import static com.artbook.dao.util.Preconditions.requireNonEmpty;
import static java.util.Objects.requireNonNull;

public record ImageSavedSummary(UUID globalId, String contentType, Map<ImageType, Object> byType) {
    public ImageSavedSummary {
        requireNonNull(globalId);
        requireNonEmpty(contentType);
        requireNonEmpty(byType);
    }
}

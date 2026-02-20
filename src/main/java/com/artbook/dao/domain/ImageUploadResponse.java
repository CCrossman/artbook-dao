package com.artbook.dao.domain;

import java.util.UUID;

import static com.artbook.dao.util.Preconditions.requireNonEmpty;
import static java.util.Objects.requireNonNull;

public record ImageUploadResponse(UUID globalId, String errorMessage, ImageSavedSummary summary) {
    public ImageUploadResponse {
        requireNonNull(globalId);

        if (errorMessage == null || errorMessage.isBlank()) {
            requireNonEmpty(summary);
        } else if (summary == null) {
            requireNonEmpty(errorMessage);
        }
    }
}

package com.artbook.dao.domain;

import static com.artbook.dao.util.Preconditions.requireNonEmpty;

public record ImageUploadResponse(Long imageId, String message) {
    public ImageUploadResponse {
        if (imageId == null) {
            requireNonEmpty(message, "message cannot be empty if imageId is null");
        }
    }
}

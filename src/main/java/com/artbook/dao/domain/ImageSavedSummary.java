package com.artbook.dao.domain;

import io.vavr.control.Try;

import java.util.Map;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record ImageSavedSummary(UUID globalId, Map<ImageType, Try<Boolean>> subTaskCompletion, Try<Boolean> reducedCompletion) {
    public ImageSavedSummary {
        requireNonNull(globalId);
        requireNonNull(subTaskCompletion);
        requireNonNull(reducedCompletion);
    }
}

package com.artbook.dao.domain;

import io.vavr.control.Try;

import static java.util.Objects.requireNonNull;

public record ImageSavedSummaryByType(ImageType imageType, Try<Boolean> completion) {
    public ImageSavedSummaryByType {
        requireNonNull(imageType);
        requireNonNull(completion);
    }
}

package com.artbook.dao.domain;

import static com.artbook.dao.util.Preconditions.requireNonEmpty;

public record ImageTag(String key, String value) {
    public ImageTag {
        requireNonEmpty(key);
        requireNonEmpty(value);
    }

    public static ImageTag fromEncodedString(String encodedString) {
        int idx = encodedString == null ? -1 : encodedString.indexOf(":");
        if (idx == -1) {
            return new ImageTag(encodedString, "");
        }
        String key = encodedString.substring(0, idx);
        String value = encodedString.substring(idx + 1);
        return new ImageTag(key, value);
    }
}

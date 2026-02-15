package com.artbook.dao.domain;

public enum ImageType {
    FULL,
    PREVIEW,
    THUMBNAIL,
    TWITTER;

    public static ImageType fromString(String imageTypeName) {
        for (ImageType imageType : values()) {
            if (imageType.name().equalsIgnoreCase(imageTypeName)) {
                return imageType;
            }
        }
        return null;
    }
}

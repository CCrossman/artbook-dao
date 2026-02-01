package com.artbook.dao.domain;

import org.springframework.core.io.Resource;

import java.util.List;

public record ImageDTO(
    long imageId, String title, String description, Integer likes,
    Boolean liked, List<ImageTag> tags, Resource imageUrl,
    String contentType
) {
}

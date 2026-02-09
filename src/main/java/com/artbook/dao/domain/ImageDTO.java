package com.artbook.dao.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.core.io.Resource;

import java.util.List;

@Builder
@EqualsAndHashCode
@Getter
@ToString
public class ImageDTO {
    private Long imageId;
    private String title;
    private String description;
    private Integer likes;
    private Boolean liked;
    private List<ImageTag> tags;
    private Resource resource;
    private String contentType;
}
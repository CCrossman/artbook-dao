package com.artbook.dao.service;

import com.artbook.dao.domain.ImageDTO;
import com.artbook.dao.domain.ImageTag;
import com.artbook.dao.entity.ImageEntity;
import com.artbook.dao.util.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class DefaultImageEntityConverter implements Converter<ImageEntity, ImageDTO> {
    private static final Logger logger = LoggerFactory.getLogger(DefaultImageEntityConverter.class);

    @Autowired
    private Converter<String, ImageTag> imageTagConverter;

    @Autowired
    private Converter<String, Resource> resourceConverter;

    @Override
    public ImageDTO convert(ImageEntity item) throws Exception {
        ImageDTO.ImageDTOBuilder b = ImageDTO.builder();

        if (item != null) {
            b.imageId(item.getId());
            b.title(item.getTitle());
            b.description(item.getDescription());
            b.tags(convertTags(item.getTags()));
            b.resource(resourceConverter.convert(item.getUri()));
            b.contentType(item.getContentType());
//            b.liked(item.isLiked());
//            b.likes(item.getLikes());
        }
        return b.build();
    }

    private List<ImageTag> convertTags(Collection<String> tags) throws Exception {
        List<ImageTag> imageTags = new ArrayList<>(tags.size());

        for (String tag : tags) {
            ImageTag imageTag = imageTagConverter.convert(tag);
            if (imageTag != null) {
                imageTags.add(imageTag);
            }
        }
        return imageTags;
    }

}

package com.artbook.dao.service;

import com.artbook.dao.domain.ImageDTO;
import com.artbook.dao.domain.ImageTag;
import com.artbook.dao.entity.ImageEntity;
import com.artbook.dao.util.Converter;
import com.jecklgamis.util.Try;
import com.jecklgamis.util.TryFactory;
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
    public Try<ImageDTO> convert(ImageEntity item) {
        return TryFactory.attempt(() -> {
            ImageDTO.ImageDTOBuilder b = ImageDTO.builder();

            if (item != null) {
                b.imageId(item.getId());
                b.title(item.getTitle());
                b.description(item.getDescription());
                b.tags(convertTags(item.getTags()));
                b.resource(convertResource(item.getUri()));
                b.contentType(item.getContentType());
                b.liked(item.isLiked());
                b.likes(item.getLikes());
            }
            return b.build();
        });
    }

    private List<ImageTag> convertTags(Collection<String> tags) {
        List<ImageTag> imageTags = new ArrayList<>(tags.size());

        for (String tag : tags) {
            Try<ImageTag> imageTagTry = imageTagConverter.convert(tag);
            if (imageTagTry != null) {
                imageTagTry.forEach(imageTags::add);
            }
        }
        return imageTags;
    }

    private Resource convertResource(String uri) {
        return resourceConverter.convert(uri).get();
    }
}

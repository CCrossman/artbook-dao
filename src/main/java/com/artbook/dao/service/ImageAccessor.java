package com.artbook.dao.service;

import com.artbook.dao.domain.ImageDTO;
import com.artbook.dao.domain.ImageTag;
import com.artbook.dao.domain.ImageType;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class ImageAccessor {
    private static final Logger logger = LoggerFactory.getLogger(ImageAccessor.class);

    @Value("${app.location.images.full}")
    private String fullImageLocation;

    @Value("${app.location.images.preview}")
    private String previewImageLocation;

    @Value("${app.location.images.thumbnail}")
    private String thumbnailImageLocation;

    @Value("${app.location.images.twitter}")
    private String twitterImageLocation;

    private Map<ImageType, String> imageTypeToLocation;

    @PostConstruct
    public void init() {
        this.imageTypeToLocation = new EnumMap<>(ImageType.class);
        imageTypeToLocation.put(ImageType.FULL, fullImageLocation);
        imageTypeToLocation.put(ImageType.PREVIEW, previewImageLocation);
        imageTypeToLocation.put(ImageType.THUMBNAIL, thumbnailImageLocation);
        imageTypeToLocation.put(ImageType.TWITTER, twitterImageLocation);
        logger.info("imageTypeToLocation: {}", imageTypeToLocation);
    }

    public ImageDTO getImageFile(long imageId, ImageType imageType, String contentType) {
        logger.info("getImageFile: {}, {}, {}", imageId, imageType, contentType);

        String location = imageTypeToLocation.get(imageType);
        File file = new File(location + File.separator + imageId);
        Resource resource = new FileSystemResource(file);

        // TODO
        String title = null;
        String description = null;
        Integer likes = null;
        Boolean liked = null;
        List<ImageTag> tags = null;

        return new ImageDTO(imageId, title, description, likes, liked, tags, resource, contentType);
    }

    public String getImageContentType(long imageId) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public ImageType getImageType(long imageId) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Long uploadImage(MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Image file is empty");
        }
        String filename = image.getOriginalFilename();
        logger.info("Uploading image: {}", filename);

        long fileSize = image.getSize();
        logger.info("Image size: {} bytes", fileSize);

        String contentType = image.getContentType();
        logger.info("Content type: {}", contentType);

        byte[] fileContent = image.getBytes();
        logger.info("Image content: {} bytes", fileContent.length);

        // TODO
        return null;
    }
}

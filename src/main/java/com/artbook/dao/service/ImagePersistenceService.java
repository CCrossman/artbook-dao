package com.artbook.dao.service;

import com.artbook.dao.domain.ImageSavedSummary;
import com.artbook.dao.domain.ImageType;
import com.artbook.dao.repository.ImageRepository;
import io.vavr.control.Try;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ImagePersistenceService {
    private static final Logger logger = LoggerFactory.getLogger(ImagePersistenceService.class);

    @Autowired
    private ImageRepository imageRepository;

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

    public Try<ImageSavedSummary> saveImage(UUID globalId, MultipartFile image) {
        logger.info("saveImage({},...)", globalId);

        String contentType = image.getContentType();
        logger.info("- contentType: {}", contentType);

        String name = image.getName();
        logger.info("- name: {}", name);

        //Resource resource = image.getResource();
        //byte[] content = image.getBytes();

        long size = image.getSize();
        logger.info("- size in bytes: {}", size);

        String originalFilename = image.getOriginalFilename();
        logger.info("- originalFilename: {}", originalFilename);

        // asynchronous operations, use image.transferTo(...)
        // TODO: save full image
        // TODO: save preview image
        // TODO: save thumbnail image
        // TODO: save twitter card image

        return null;
    }
}

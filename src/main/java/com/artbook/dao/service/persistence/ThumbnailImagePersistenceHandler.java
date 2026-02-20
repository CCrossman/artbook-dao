package com.artbook.dao.service.persistence;

import com.artbook.dao.domain.ImageSavedSummaryByType;
import com.artbook.dao.domain.ImageType;
import com.artbook.dao.service.ImagePersistenceHandler;
import io.vavr.concurrent.Future;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
public class ThumbnailImagePersistenceHandler implements ImagePersistenceHandler {
    private static final Logger logger = LoggerFactory.getLogger(ThumbnailImagePersistenceHandler.class);

    @Value("${app.location.images.thumbnail}")
    private String thumbnailImageLocation;

    @Override
    public Future<ImageSavedSummaryByType> save(UUID globalId, MultipartFile image) {
        return Future.of(() -> {
            logger.info("Saved {} multipart file as {}!", globalId, ImageType.THUMBNAIL);
            return new ImageSavedSummaryByType(ImageType.THUMBNAIL, Try.success(false));
        });
    }
}

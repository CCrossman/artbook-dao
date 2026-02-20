package com.artbook.dao.service;

import com.artbook.dao.domain.ImageSavedSummaryByType;
import io.vavr.concurrent.Future;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ImagePersistenceHandler {
    public Future<ImageSavedSummaryByType> save(UUID globalId, MultipartFile image);
}

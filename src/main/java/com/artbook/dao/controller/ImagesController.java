package com.artbook.dao.controller;

import com.artbook.dao.domain.*;
import com.artbook.dao.service.ImagePersistenceService;
import com.artbook.dao.service.ImageQueryService;
import io.vavr.concurrent.Future;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

@RestController
@RequestMapping("/api/v1/images")
public class ImagesController {
    private static final Logger logger = LoggerFactory.getLogger(ImagesController.class);

    @Autowired
    private ImageQueryService imageQueryService;

    @Autowired
    private ImagePersistenceService imagePersistenceService;

    @GetMapping
    public ResponseEntity<Page<ImageDTO>> getImageMetadata(@RequestParam MultiValueMap<String, String> queryParams) {
        logger.info("getImageMetadata: {}", queryParams);
        try {
            Page<ImageDTO> page = imageQueryService.getImageMetadata(ImageType.THUMBNAIL, queryParams);
            return ResponseEntity.ok(page);
        } catch (Exception ex) {
            logger.error("Error reading images", ex);
            return ResponseEntity.internalServerError()
                .header("x-error-type", ex.getClass().getName())
                .header("x-error-message", ex.getMessage())
                .build();
        }
    }

    @GetMapping("/{imageId}/{imageType}")
    public ResponseEntity<ImageDTO> getImageMetadata(@PathVariable long imageId, @PathVariable String imageType) {
        logger.info("getImageMetadata: {}, {}", imageId, imageType);
        try {
            ImageType it = ImageType.fromString(imageType);
            if (it == null) {
                logger.warn("Image Type not found: {}", imageType);
                return ResponseEntity.badRequest().build();
            }
            ImageDTO dto = imageQueryService.getImageMetadata(it, imageId);
            if (dto == null) {
                logger.warn("Image not found: {} {}", imageId, imageType);
                return ResponseEntity.badRequest().build();
            }
            logger.atDebug().log("Image found: {}", dto);
            return ResponseEntity.ok(dto);
        } catch (Exception ex) {
            logger.error("Error reading image file", ex);
            return ResponseEntity.internalServerError()
                .header("x-image-id", String.valueOf(imageId))
                .header("x-image-type", imageType)
                .header("x-error-type", ex.getClass().getName())
                .header("x-error-message", ex.getMessage())
                .build();
        }
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ImageUploadResponse> uploadImage(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile image,
            @RequestParam(value = "tags", required = false) List<String> tags
    ) {
        UUID globalId = UUID.randomUUID();
        try {
            requireNonNull(description);
            requireNonNull(image);
            requireNonNull(title);

            ImageTagList imageTags = tags == null ? null : ImageTagList.fromRequestParameter(tags);

            logger.info("uploadImage");
            logger.info("- uuid: {}", globalId);
            logger.info("- title: {}", title);
            logger.info("- description: {}", description);
            logger.info("- image: {}", image);
            logger.info("- tags: {}", imageTags);

            Future<ImageSavedSummary> futSummary = imagePersistenceService.saveImage(globalId, image);
            if (futSummary == null) {
                logger.error("Failed to summarize image, but no error thrown.");
                return ResponseEntity.internalServerError().build();
            }

            futSummary.await(15, TimeUnit.SECONDS);

            Try<ImageSavedSummary> valueOrError = futSummary.getValue().getOrNull();
            if (valueOrError == null) {
                logger.error("Failed to summarize image, but no error thrown.");
                return ResponseEntity.internalServerError().build();
            }

            if (valueOrError.isFailure()) {
                Throwable error = requireNonNull(valueOrError.getCause());

                StringBuilder sb = new StringBuilder();
                sb.append(error.getClass().getName());

                if (error.getMessage() != null && !error.getMessage().isEmpty()) {
                    sb.append(": ").append(error.getMessage());
                }
                return ResponseEntity.ok(new ImageUploadResponse(globalId, sb.toString(), null));
            }

            ImageSavedSummary summary = valueOrError.get();
            return ResponseEntity.ok(new ImageUploadResponse(globalId, null, summary));
        } catch (Exception ex) {
            logger.error("Problem uploading image.", ex);
            return ResponseEntity.internalServerError()
                .header("x-image-id", String.valueOf(globalId))
                .header("x-error-type", ex.getClass().getName())
                .header("x-error-message", ex.getMessage())
                .build();
        }
    }
}

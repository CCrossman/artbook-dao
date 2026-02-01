package com.artbook.dao.controller;

import com.artbook.dao.domain.*;
import com.artbook.dao.service.ImageAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1/images")
public class ImagesController {
    private static final Logger logger = LoggerFactory.getLogger(ImagesController.class);

    @Autowired
    private ImageAccessor imageAccessor;

    @GetMapping
    public Page<ImageDTO> getImages(@RequestParam MultiValueMap<String, String> queryParams) {
        logger.info("getImages: {}", queryParams);

        String title = queryParams.getFirst("title");
        logger.info("title: {}", title);

        List<ImageTag> tags = Optional.ofNullable(queryParams.get("tags"))
            .map(lst -> lst.stream().map(ImageTag::fromEncodedString).toList())
            .orElse(Collections.emptyList());
        logger.info("tags: {}", tags);

        // TODO
        Object startDate = queryParams.getFirst("startDate");
        logger.info("startDate: {}", startDate);

        // TODO
        Object endDate = queryParams.getFirst("endDate");
        logger.info("endDate: {}", endDate);

        int pageNo = Optional.ofNullable(queryParams.getFirst("pageNo"))
            .map(Integer::parseInt)
            .orElse(1);
        logger.info("pageNo: {}", pageNo);

        int pageSize = Optional.ofNullable(queryParams.getFirst("pageSize"))
            .map(Integer::parseInt)
            .orElse(10);
        logger.info("pageSize: {}", pageSize);

        String sortBy = queryParams.getFirst("sortBy");
        logger.info("sortBy: {}", sortBy);

        SortOrder sortOrder = Optional.ofNullable(queryParams.getFirst("sortOrder"))
            .map(SortOrder::valueOf)
            .orElse(SortOrder.UNSORTED);
        logger.info("sortOrder: {}", sortOrder);

        // TODO
        throw new UnsupportedOperationException();
    }

    @GetMapping("/{imageId}/{imageType}")
    public ResponseEntity<ImageDTO> getImage(@PathVariable long imageId, @PathVariable String imageType) {
        logger.info("getImage: {}, {}", imageId, imageType);
        try {
            if (imageType == null) {
                logger.warn("Image Type not found: {}", imageId);
                return ResponseEntity.notFound().build();
            }

            String contentType = imageAccessor.getImageContentType(imageId);
            if (contentType == null || contentType.isEmpty()) {
                logger.warn("Content Type not found: {}", imageId);
                return ResponseEntity.notFound().build();
            }

            ImageType type = ImageType.valueOf(imageType.toUpperCase());
            ImageDTO imageDTO = imageAccessor.getImageFile(imageId, type, contentType);
            if (imageDTO == null) {
                logger.warn("Image not found: {}", imageId);
                return ResponseEntity.notFound().build();
            }

            logger.atDebug().log("Image found: {}", imageDTO);
            return ResponseEntity.ok(imageDTO);
        } catch (Exception ex) {
            logger.error("Error reading image file", ex);
            return ResponseEntity.internalServerError()
                .header("x-image-id", String.valueOf(imageId))
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
        logger.info("uploadImage");
        logger.info("title: {}", title);
        logger.info("description: {}", description);
        logger.info("image: {}", image);
        logger.info("tags: {}", tags);

        try {
            Long imageId = imageAccessor.uploadImage(image);
            logger.atDebug().log("Image uploaded successfully. ImageId: {}", imageId);

            if (imageId == null) {
                return ResponseEntity.unprocessableEntity().body(new ImageUploadResponse(null, "Failed to upload image but not clear why."));
            }
            return ResponseEntity.ok(new ImageUploadResponse(imageId, null));
        } catch (Exception e) {
            logger.error("Error reading image file", e);
            String message = e.getClass().getName() + ": " + e.getMessage();
            return ResponseEntity.internalServerError().body(new ImageUploadResponse(null, message));
        }
    }
}

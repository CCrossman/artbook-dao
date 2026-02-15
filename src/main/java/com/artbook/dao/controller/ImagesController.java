package com.artbook.dao.controller;

import com.artbook.dao.domain.*;
import com.artbook.dao.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/v1/images")
public class ImagesController {
    private static final Logger logger = LoggerFactory.getLogger(ImagesController.class);

    @Autowired
    private ImageService imageService;

    @GetMapping
    public ResponseEntity<Page<ImageDTO>> getImages(@RequestParam MultiValueMap<String, String> queryParams) {
        logger.info("getImages: {}", queryParams);
        try {
            Page<ImageDTO> page = imageService.getImages(ImageType.THUMBNAIL, queryParams);
            return ResponseEntity.ok(page);
        } catch (Exception e) {
            logger.error("Error reading images", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{imageId}/{imageType}")
    public ResponseEntity<ImageDTO> getImage(@PathVariable long imageId, @PathVariable String imageType) {
        logger.info("getImage: {}, {}", imageId, imageType);
        try {
            ImageType it = ImageType.fromString(imageType);
            if (it == null) {
                logger.warn("Image Type not found: {}", imageType);
                return ResponseEntity.badRequest().build();
            }
            ImageDTO dto = imageService.getImage(imageId, it);
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
        logger.info("uploadImage");
        logger.info("title: {}", title);
        logger.info("description: {}", description);
        logger.info("image: {}", image);
        logger.info("tags: {}", tags);

        try {
//            Long imageId = imageAccessor.uploadImage(image);
//            logger.atDebug().log("Image uploaded successfully. ImageId: {}", imageId);
//
//            if (imageId == null) {
//                return ResponseEntity.unprocessableEntity().body(new ImageUploadResponse(null, "Failed to upload image but not clear why."));
//            }
//            return ResponseEntity.ok(new ImageUploadResponse(imageId, null));
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error reading image file", e);
            String message = e.getClass().getName() + ": " + e.getMessage();
            return ResponseEntity.internalServerError().body(new ImageUploadResponse(null, message));
        }
    }
}

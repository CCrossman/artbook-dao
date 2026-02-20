package com.artbook.dao.service;

import com.artbook.dao.domain.ImageDTO;
import com.artbook.dao.domain.ImageTagList;
import com.artbook.dao.domain.ImageType;
import com.artbook.dao.entity.ImageEntity;
import com.artbook.dao.repository.ImageEntitySpecifications;
import com.artbook.dao.repository.ImageRepository;
import com.artbook.dao.util.Converter;
import io.micrometer.common.util.StringUtils;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.util.Objects.requireNonNull;

@Service
public class ImageQueryService {
    private static final Logger logger = LoggerFactory.getLogger(ImageQueryService.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 100;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private Converter<ImageEntity,ImageDTO> imageConverter;

    public ImageDTO getImageMetadata(ImageType imageType, long imageId) {
        logger.info("getImageMetadata({},{})", imageType, imageId);
        requireNonNull(imageType);

        List<Specification<ImageEntity>> specs = new ArrayList<>();
        specs.add(ImageEntitySpecifications.hasId(imageId));
        specs.add(ImageEntitySpecifications.isImageType(imageType));

        return imageRepository
            .findOne(ImageEntitySpecifications.all(specs))
            .map(ie -> imageConverter.convertUnchecked(ie))
            .orElse(null);
    }

    public Page<ImageDTO> getImageMetadata(ImageType imageType, MultiValueMap<String, String> queryParams) {
        logger.info("getImageMetadata({},{})", imageType, queryParams);

        int oneIndexedPageNo = Try.ofSupplier(() -> queryParams.getFirst("pageNo"))
            .map(Integer::parseInt)
            .getOrElse(() -> DEFAULT_PAGE_NUMBER);
        logger.info("pageNo: {}", oneIndexedPageNo);

        int pageSize = Try.ofSupplier(() -> queryParams.getFirst("pageSize"))
            .map(Integer::parseInt)
            .getOrElse(() -> DEFAULT_PAGE_SIZE);
        logger.info("pageSize: {}", pageSize);

        if (pageSize > MAX_PAGE_SIZE) {
            logger.warn("Page Size is limited to " + MAX_PAGE_SIZE + " or less");
            pageSize = MAX_PAGE_SIZE;
        }

        String sortBy = queryParams.getFirst("sortBy");
        logger.info("sortBy: {}", sortBy);

        String sortOrder = queryParams.getFirst("sortOrder");
        logger.info("sortOrder: {}", sortOrder);

        Pageable pageable = PageRequest.of(oneIndexedPageNo, pageSize, getSort(sortBy, sortOrder));

        // definitely an empty page
        if (pageSize <= 0 || oneIndexedPageNo < 1) {
            return Page.empty(pageable);
        }

        List<Specification<ImageEntity>> specs = new ArrayList<>();

        logger.info("imageType: {}", imageType);
        if (imageType != null) {
            specs.add(ImageEntitySpecifications.isImageType(imageType));
        }

        String title = queryParams.getFirst("title");
        logger.info("title: {}", title);

        if (StringUtils.isNotBlank(title)) {
            specs.add(ImageEntitySpecifications.titleContains(title));
        }

        ImageTagList tags = Try.ofSupplier(() -> queryParams.get("tags"))
            .filter(Objects::nonNull)
            .map(ImageTagList::fromRequestParameter)
            .getOrElse(() -> ImageTagList.EMPTY);
        logger.info("tags: {}", tags);

        if (tags != null) {
            specs.add(ImageEntitySpecifications.hasTags(tags.toMap()));
        }

        ZonedDateTime startDate = Try.ofSupplier(() -> queryParams.getFirst("startDate"))
            .filter(Objects::nonNull)
            .flatMap(s -> Try.ofCallable(() -> ZonedDateTime.from(formatter.parse(s))))
            .getOrElse(() -> null);
        logger.info("startDate: {}", startDate);

        if (startDate != null) {
            specs.add(ImageEntitySpecifications.createdAfter(startDate));
        }

        ZonedDateTime endDate = Try.ofSupplier(() -> queryParams.getFirst("endDate"))
            .filter(Objects::nonNull)
            .flatMap(s -> Try.ofCallable(() -> ZonedDateTime.from(formatter.parse(s))))
            .getOrElse(() -> null);
        logger.info("endDate: {}", endDate);

        if (endDate != null) {
            specs.add(ImageEntitySpecifications.createdBefore(endDate));
        }

        Specification<ImageEntity> specifications = ImageEntitySpecifications.all(specs);
        return imageRepository
            .findAll(specifications, pageable)
            .map(imageEntity -> imageConverter.convertUnchecked(imageEntity));
    }

    private static Sort getSort(String sortBy, String sortOrder) {
        if ("asc".equalsIgnoreCase(sortOrder) || "ascending".equalsIgnoreCase(sortOrder)) {
            return Sort.by(Sort.Direction.ASC, sortBy);
        }
        if ("desc".equalsIgnoreCase(sortOrder) || "descending".equalsIgnoreCase(sortOrder)) {
            return Sort.by(Sort.Direction.DESC, sortBy);
        }
        logger.error("Unrecognized sort parameters: by={}, order={}", sortBy, sortOrder);
        return Sort.unsorted();
    }
}

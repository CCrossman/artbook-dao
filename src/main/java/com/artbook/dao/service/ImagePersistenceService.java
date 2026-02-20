package com.artbook.dao.service;

import com.artbook.dao.domain.ImageSavedSummary;
import com.artbook.dao.domain.ImageSavedSummaryByType;
import com.artbook.dao.repository.ImageRepository;
import io.vavr.Tuple;
import io.vavr.collection.Seq;
import io.vavr.concurrent.Future;
import io.vavr.control.Try;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.UUID;

@Service
public class ImagePersistenceService {
    private static final Logger logger = LoggerFactory.getLogger(ImagePersistenceService.class);

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private Collection<ImagePersistenceHandler> imageTypeHandlers;

    @PostConstruct
    public void init() {
        logger.debug("imageTypeHandlers: {}", imageTypeHandlers);
    }

    public Future<ImageSavedSummary> saveImage(UUID globalId, MultipartFile image) {
        logger.info("saveImage({},...)", globalId);

        // asynchronous operations, use image.transferTo(...)
        Future<Seq<ImageSavedSummaryByType>> allDone = Future.sequence(imageTypeHandlers.stream()
            .map(handler -> handler.save(globalId, image))
            .toList());

        return allDone.map(summaries -> combineSummaries(globalId, summaries));
    }

    private ImageSavedSummary combineSummaries(UUID globalId, Seq<ImageSavedSummaryByType> summaries) {
        var subTaskCompletion = summaries.toJavaMap(summary -> Tuple.of(summary.imageType(), summary.completion()));

        var reducedCompletion = subTaskCompletion.values().stream()
            .reduce(Try.success(true), (a,b) -> a.flatMap(aa -> b.map(bb -> aa && bb)));

        return new ImageSavedSummary(globalId, subTaskCompletion, reducedCompletion);
    }
}

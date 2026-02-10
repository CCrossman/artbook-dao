package com.artbook.dao.service;

import com.artbook.dao.entity.ImageEntity;
import com.artbook.dao.repository.ImageRepository;
import com.artbook.dao.repository.RepositoryFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class ImageService {

    @Autowired
    public ImageRepository imageRepository;

    public Page<ImageEntity> getImagesByFilter(RepositoryFilter<ImageEntity> filter) {
        final long totalCount;
        try (Stream<ImageEntity> stream = imageRepository.streamAllBy(Sort.unsorted())) {
            totalCount = stream.filter(filter).count();
        }

        try (Stream<ImageEntity> stream = imageRepository.streamAllBy(filter.getOrder())) {
            List<ImageEntity> items = stream.filter(filter)
                .skip(filter.getOffset())
                .limit(filter.getLimit())
                .toList();

            return new PageImpl<>(items, filter.getPageable(), totalCount);
        }
    }
}

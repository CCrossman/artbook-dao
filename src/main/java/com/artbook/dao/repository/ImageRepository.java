package com.artbook.dao.repository;

import com.artbook.dao.entity.ImageEntity;
import jakarta.persistence.QueryHint;
import org.hibernate.jpa.AvailableHints;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface ImageRepository extends PagingAndSortingRepository<ImageEntity, Long> {

    @QueryHints(@QueryHint(name = AvailableHints.HINT_FETCH_SIZE, value = "100"))
    @Query("select i from ImageEntity i")
    public Stream<ImageEntity> streamAllBy(Sort sort);
}

package com.artbook.dao.repository;

import com.artbook.dao.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends PagingAndSortingRepository<ImageEntity, Long>, JpaSpecificationExecutor<ImageEntity> {

}

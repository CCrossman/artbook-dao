package com.artbook.dao.repository;

import com.artbook.dao.util.CheckedPredicate;
import lombok.Builder;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.function.Predicate;

@Builder
public final class RepositoryFilter<Entity> implements Predicate<Entity> {
    private static final Logger logger = LoggerFactory.getLogger(RepositoryFilter.class);

    @Getter
    private final Sort order;

    @Getter
    private final int limit, offset;

    @Getter
    private final Pageable pageable;

    private final int pageNumberOneIndexed;
    private final Predicate<Entity> compositeFilter;

    private RepositoryFilter(int pageNumberOneIndexed, int pageSize, List<Sort.Order> ordering, List<CheckedPredicate<Entity>> filters) {
        this.limit = pageSize;
        this.offset = (pageNumberOneIndexed - 1) * pageSize;
        this.order = (ordering == null ? Sort.unsorted() : Sort.by(ordering));
        this.pageNumberOneIndexed = pageNumberOneIndexed;

        this.compositeFilter = entity -> {
            if (filters != null) {
                for (CheckedPredicate<Entity> filter : filters) {
                    try {
                        if (!filter.test(entity)) {
                            return false;
                        }
                    } catch (Exception e) {
                        logger.error("Error processing entity: {}", entity, e);
                        return true;
                    }
                }
            }
            return true;
        };

        this.pageable = PageRequest.of(pageNumberOneIndexed, limit, getOrder());
    }

    public boolean test(Entity entity) {
        return compositeFilter.test(entity);
    }
}

package com.artbook.dao.repository;

import com.artbook.dao.domain.ImageType;
import com.artbook.dao.entity.ImageEntity;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.function.Supplier;

public class ImageEntitySpecifications {

    private ImageEntitySpecifications() {
        // do not extend or instantiate
    }

    public static Specification<ImageEntity> all(Collection<Specification<ImageEntity>> specifications) {
        return (root, query, cb) -> {
            Predicate[] predicates = specifications.stream()
                .map(spec -> spec.toPredicate(root, query, cb))
                .toArray(Predicate[]::new);
            return cb.and(predicates);
        };
    }

    public static Specification<ImageEntity> createdAfter(ZonedDateTime zdt) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("created_at"), zdt);
    }

    public static Specification<ImageEntity> createdBefore(ZonedDateTime zdt) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("created_at"), zdt);
    }

//    public static Specification<ImageEntity> includesTags(List<ImageTag> tags) {
//        return (root, query, cb) -> {
//            Expression<String> jsonbPath = whereJson(root, cb, () -> cb.literal("tags"));
//        };
//    }

    public static Specification<ImageEntity> isImageType(ImageType imageType) {
        String imageTypeInDatabase = imageType.name().toUpperCase();
        return withJsonPropertyEqualTo("imageType", imageTypeInDatabase);
    }

    public static Specification<ImageEntity> titleContains(String substr) {
        return withJsonPropertyLike("title", substr);
    }

    private static Specification<ImageEntity> withJsonPropertyEqualTo(String propertyKey, String propertyValue) {
        return (root, query, cb) -> {
            Expression<String> jsonbPath = whereJson(root, cb, () -> cb.literal(propertyKey));
            return cb.equal(jsonbPath, propertyValue);
        };
    }

    private static Specification<ImageEntity> withJsonPropertyLike(String propertyKey, String propertyValue) {
        return (root, query, cb) -> {
            Expression<String> jsonbPath = whereJson(root, cb, () -> cb.literal(propertyKey));
            return cb.like(jsonbPath, "%" + propertyValue + "%");
        };
    }

    private static Expression<String> whereJson(Root<ImageEntity> root, CriteriaBuilder builder, Supplier<Expression<?>> toMatcher) {
        return builder.function(
            "jsonb_extract_path_text", // PostgreSQL function name
            String.class,
            root.get("properties"),
            toMatcher.get()
        );
    }
}

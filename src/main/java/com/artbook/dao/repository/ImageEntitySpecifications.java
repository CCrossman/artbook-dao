package com.artbook.dao.repository;

import ch.qos.logback.core.util.StringUtil;
import com.artbook.dao.domain.ImageType;
import com.artbook.dao.entity.ImageEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class ImageEntitySpecifications {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private ImageEntitySpecifications() {
        // do not extend or instantiate
    }

    public static Specification<ImageEntity> all(Collection<Specification<ImageEntity>> specifications) {
        return (root, query, cb) -> {
            if (specifications == null || specifications.isEmpty()) {
                return cb.conjunction(); // returns "true" (no filter)
            }
            Predicate[] predicates = specifications.stream()
                .map(spec -> spec.toPredicate(root, query, cb))
                .toArray(Predicate[]::new);
            return cb.and(predicates);
        };
    }

    public static Specification<ImageEntity> createdAfter(ZonedDateTime zdt) {
        requireNonNull(zdt);
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("created_at"), zdt);
    }

    public static Specification<ImageEntity> createdBefore(ZonedDateTime zdt) {
        requireNonNull(zdt);
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("created_at"), zdt);
    }

    public static Specification<ImageEntity> hasId(long imageId) {
        return withJsonPropertyEqualTo("id", imageId);
    }

    public static Specification<ImageEntity> hasTags(Map<String,String> searchTags) {
        return (root, query, cb) -> {
            if (searchTags == null || searchTags.isEmpty()) {
                return cb.conjunction(); // returns "true" (no filter)
            }
            // because the frontend will always give us String tag values, and jsonb '@>'
            // will not convert types when matching, we MUST make sure the JSON tag property
            // in the database stores only String keys and String values. This should be
            // fine though since the frontend will display tags in the HTML (basically Strings).
            try {
                // Convert the Java Map to a JSON String
                // Example: {"camera": "Sony", "lens": "50mm", "shutterSpeed": "100"}
                Map<String,Map<String,String>> searchJson = Map.of("tags", searchTags);
                String jsonString = objectMapper.writeValueAsString(searchJson);

                // Call the custom SQL function "jsonb_match"
                // usage: jsonb_match(table.tags, '{"camera":"Sony"}')
                return cb.isTrue(
                    cb.function(
                        "jsonb_match",   // Function name in DB
                        Boolean.class,   // Return type
                        root.get("properties"), // Column
                        cb.literal(jsonString) // The JSON argument
                    )
                );
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to serialize tags", e);
            }
        };
    }

    public static Specification<ImageEntity> isImageType(ImageType imageType) {
        String imageTypeInDatabase = (imageType == null ? null : imageType.toDatabaseValue());
        return withJsonPropertyEqualTo("imageType", imageTypeInDatabase);
    }

    public static Specification<ImageEntity> titleContains(String substr) {
        return withJsonPropertyLike("title", substr);
    }

    private static Specification<ImageEntity> withJsonPropertyEqualTo(String propertyKey, Object propertyValue) {
        return (root, query, cb) -> {
            if (StringUtil.isNullOrEmpty(propertyKey)) {
                return cb.conjunction(); // returns "true" (no filter)
            }
            Expression<String> jsonbPath = whereJson(root, cb, () -> cb.literal(propertyKey));
            return cb.equal(jsonbPath, propertyValue);
        };
    }

    private static Specification<ImageEntity> withJsonPropertyLike(String propertyKey, String propertyValue) {
        return (root, query, cb) -> {
            if (StringUtil.isNullOrEmpty(propertyKey) || StringUtil.isNullOrEmpty(propertyValue)) {
                return cb.conjunction(); // returns "true" (no filter)
            }
            Expression<String> jsonbPath = whereJson(root, cb, () -> cb.literal(propertyKey));
            return cb.like(jsonbPath, "%" + propertyValue + "%");
        };
    }

    private static Expression<String> whereJson(Root<ImageEntity> root, CriteriaBuilder builder, Supplier<Expression<?>> toMatcher) {
        requireNonNull(root);
        requireNonNull(builder);
        requireNonNull(toMatcher);
        return builder.function(
            "jsonb_extract_path_text", // PostgreSQL function name
            String.class,
            root.get("properties"),
            toMatcher.get()
        );
    }
}

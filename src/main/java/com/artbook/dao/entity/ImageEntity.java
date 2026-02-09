package com.artbook.dao.entity;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;
import org.hibernate.annotations.Type;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Builder
@EqualsAndHashCode
@Getter
@ToString
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uri")
    private String uri;

    @Type(JsonBinaryType.class)
    @Column(name = "properties", columnDefinition = "jsonb")
    private Map<String, Object> properties;

    @Column(name = "created_at")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime updatedAt;

    public String getDescription() {
        return (String) properties.get("description");
    }

    public Set<String> getTags() {
        Object rawTags = properties.get("tags");
        if (rawTags == null) {
            return Collections.emptySet();
        }
        Collection<?> tags = (Collection<?>) rawTags;
        return tags.stream().map(Object::toString).collect(Collectors.toCollection(TreeSet::new));
    }

    public String getTitle() {
        return (String) properties.get("title");
    }
}

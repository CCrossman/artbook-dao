package com.artbook.dao.entity;

import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;
import org.hibernate.annotations.Type;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
@Entity
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Table(name = "images")
@ToString
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uri")
    private String uri;

    @Type(JsonBinaryType.class)
    @Column(name = "properties", columnDefinition = "jsonb")
    private JsonNode properties;

    @Column(name = "created_at")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime updatedAt;

    public String getContentType() {
        return properties.path("contentType").asText();
    }

    public String getDescription() {
        return properties.path("description").asText();
    }

//    public Integer getLikes() {
//        JsonNode likeProperty = properties.path("likes");
//        if (likeProperty != null && likeProperty.isInt()) {
//            return likeProperty.intValue();
//        }
//        return null;
//    }

    public Set<String> getTags() {
        return properties.path("tags").valueStream()
            .map(JsonNode::textValue)
            .collect(Collectors.toCollection(TreeSet::new));
    }

    public String getTitle() {
        return properties.path("title").asText();
    }

//    public Boolean isLiked() {
//        return properties.path("liked").asBoolean();
//    }
}

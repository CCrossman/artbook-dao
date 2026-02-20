package com.artbook.dao.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.*;

@Data
@JsonSerialize(using = ImageTagList.CustomSerializer.class)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageTagList {
    private static final long serialVersion = 1L;

    public static final ImageTagList EMPTY = new ImageTagList(null);

    private final List<ImageTag> tags;

    public Map<String, String> toMap() {
        if (tags == null) {
            return null;
        }
        Map<String,String> m = new LinkedHashMap<>();
        for (ImageTag tag : tags) {
            m.put(tag.key(), tag.value());
        }
        return m;
    }

    public static ImageTagList fromRequestParameter(List<String> tags) {
        List<ImageTag> imageTags = new ArrayList<>();
        if (tags != null) {
            for (String tag : tags) {
                ImageTag imageTag = ImageTag.fromEncodedString(tag);
                imageTags.add(imageTag);
            }
        }
        return new ImageTagList(Collections.unmodifiableList(imageTags));
    }

    static class CustomSerializer extends JsonSerializer<ImageTagList> {
        @Override
        public void serialize(ImageTagList imageTagList, JsonGenerator jg, SerializerProvider sp) throws IOException {
            jg.writeStartObject();

            if (imageTagList != null && imageTagList.tags != null) {
                for (ImageTag imageTag : imageTagList.tags) {
                    // to make '@>' matching work from Java to Postgres, need all
                    // values to be Strings, even if they are numeric or boolean.
                    jg.writeStringField(imageTag.key(), imageTag.value());
                }
            }
            jg.writeEndObject();
        }
    }
}

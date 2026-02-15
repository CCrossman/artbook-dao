package com.artbook.dao.service;

import com.artbook.dao.util.Converter;
import com.jecklgamis.util.Try;
import com.jecklgamis.util.TryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URL;

@Component
public class DefaultResourceConverter implements Converter<String, Resource> {
    private static final Logger logger = LoggerFactory.getLogger(DefaultResourceConverter.class);

    @Override
    public Try<Resource> convert(String rawUri) {
        return TryFactory.attempt(() -> {
            if (rawUri == null || rawUri.isBlank()) {
                return null;
            }
            URL url = URI.create(rawUri).toURL();
            return new FileUrlResource(url);
        });
    }
}

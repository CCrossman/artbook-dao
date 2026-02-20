package com.artbook.dao.service;

import com.artbook.dao.util.Converter;
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
    public Resource convert(String rawUri) throws Exception {
        if (rawUri == null || rawUri.isBlank()) {
            return null;
        }
        URL url = URI.create(rawUri).toURL();
        return new FileUrlResource(url);
    }
}

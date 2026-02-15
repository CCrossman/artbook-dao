package com.artbook.dao.config;

import com.artbook.dao.domain.ImageDTO;
import com.artbook.dao.domain.ImageTag;
import com.artbook.dao.entity.ImageEntity;
import com.artbook.dao.service.DefaultImageEntityConverter;
import com.artbook.dao.service.DefaultResourceConverter;
import com.artbook.dao.util.Converter;
import com.jecklgamis.util.TryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class ImagesConfiguration {

    @Bean
    public Converter<ImageEntity, ImageDTO> imageConverter() {
        return new DefaultImageEntityConverter();
    }

    @Bean
    public Converter<String, ImageTag> imageTagConverter() {
        return item -> TryFactory.attempt(() -> {
            if (item == null) {
                return null;
            }
            String[] parts = item.split(":", 2);
            return new ImageTag(parts[0], parts[1]);
        });
    }

    @Bean
    public Converter<String, Resource> resourceConverter() {
        return new DefaultResourceConverter();
    }
}

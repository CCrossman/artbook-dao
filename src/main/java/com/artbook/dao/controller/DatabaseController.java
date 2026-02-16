package com.artbook.dao.controller;

import com.artbook.dao.domain.UserDTO;
import com.artbook.dao.entity.UserEntity;
import com.artbook.dao.repository.UserRepository;
import com.artbook.dao.util.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/database")
public class DatabaseController {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Converter<UserEntity,UserDTO> userConverter;

    @GetMapping("/user")
    public UserDTO getUser(@RequestParam(name = "id", required = false) Long id, @RequestParam(name = "email", required = false) String email) {
        logger.info("getUser: id={}, email={}", id, email);

        if (id != null) {
            return userRepository.findById(id).map(item -> userConverter.convertUnchecked(item)).orElse(null);
        }
        if (email != null && !email.isEmpty()) {
            return userRepository.findByEmail(email).map(item -> userConverter.convertUnchecked(item)).orElse(null);
        }
        logger.warn("Must provided either id or email to find a user.");
        return null;
    }
}

package com.fschoen.parlorplace.backend.datagenerator;

import com.fschoen.parlorplace.backend.entity.persistance.User;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Data
public class GeneratedData {

    private UserCollection userCollection;
    private Map<User, String> passwordCollection;

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratedData.class);

    @Data
    public static class UserCollection {
        private User user1;
    }

}

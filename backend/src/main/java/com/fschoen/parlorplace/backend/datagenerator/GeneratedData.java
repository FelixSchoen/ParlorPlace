package com.fschoen.parlorplace.backend.datagenerator;

import com.fschoen.parlorplace.backend.entity.*;
import lombok.*;
import lombok.extern.slf4j.*;

import java.util.*;

@Slf4j
@Data
public class GeneratedData {

    private UserCollection userCollection;
    private Map<User, String> passwordCollection;

    @Data
    public static class UserCollection {
        private User admin1;
        private User user1;
        private User user2;
        private User user3;

        private User neUser1; // non existent user
    }

}

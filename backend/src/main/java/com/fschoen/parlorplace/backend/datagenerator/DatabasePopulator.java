package com.fschoen.parlorplace.backend.datagenerator;

import com.fschoen.parlorplace.backend.entity.persistance.Role;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.enums.UserRole;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@Profile({"test", "setup"})
public class DatabasePopulator {

    private final UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratedData.class);

    @Autowired
    public DatabasePopulator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates and persists test data to be used by the integration tests.
     *
     * @return An object of the type {@link GeneratedData} containing links to the data
     */
    public GeneratedData generate() {
        GeneratedData generatedData = new GeneratedData();

        Map<User, String> passwordCollection = new HashMap<>();
        generatedData.setPasswordCollection(passwordCollection);

        LOGGER.info("Generating UserCollection");
        generatedData.setUserCollection(setupUserCollection(passwordCollection));

        return generatedData;
    }

    private GeneratedData.UserCollection setupUserCollection(Map<User, String> passwordCollection) {
        GeneratedData.UserCollection userCollection = new GeneratedData.UserCollection();

        //user1
        Set<Role> roles1 = new HashSet<>() {{
            add(Role.builder().role(UserRole.ROLE_USER).build());
        }};
        User user1 = User.builder()
                .username("User1")
                .nickname("User1")
                .password("$2a$10$G7E1tKKajd3S8/ORM17isOTCH0To0VkAnTjY7R4gkcgNpyLG/.tJC") // password
                .email("user1@mail.com")
                .roles(roles1)
                .build();
        user1.getRoles().forEach(role -> role.setUser(user1));

        userCollection.setUser1(userRepository.save(user1));
        userRepository.flush();
        passwordCollection.put(user1, "password");

        return userCollection;
    }

}

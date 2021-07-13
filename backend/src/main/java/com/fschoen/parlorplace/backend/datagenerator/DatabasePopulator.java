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

        //admin1
        Set<Role> rolesAdmin1 = new HashSet<>() {{
            add(Role.builder().role(UserRole.ROLE_USER).build());
            add(Role.builder().role(UserRole.ROLE_ADMIN).build());
        }};
        User admin1 = User.builder()
                .username("Admin1")
                .nickname("Admin1")
                .password("$2a$10$G7E1tKKajd3S8/ORM17isOTCH0To0VkAnTjY7R4gkcgNpyLG/.tJC")
                .email("admin1@mail.com")
                .roles(rolesAdmin1)
                .build();
        admin1.getRoles().forEach(role -> role.setUser(admin1));

        userCollection.setAdmin1(userRepository.save(admin1));
        passwordCollection.put(admin1, "password");

        //user1
        Set<Role> rolesUser1 = new HashSet<>() {{
            add(Role.builder().role(UserRole.ROLE_USER).build());
        }};
        User user1 = User.builder()
                .username("User1")
                .nickname("User1")
                .password("$2a$10$G7E1tKKajd3S8/ORM17isOTCH0To0VkAnTjY7R4gkcgNpyLG/.tJC")
                .email("user1@mail.com")
                .roles(rolesUser1)
                .build();
        user1.getRoles().forEach(role -> role.setUser(user1));

        userCollection.setUser1(userRepository.save(user1));
        passwordCollection.put(user1, "password");

        //non existant user1
        Set<Role> rolesneUser1 = new HashSet<>() {{
            add(Role.builder().role(UserRole.ROLE_USER).build());
        }};
        User neUser1 = User.builder()
                .username("nonExistentUsername")
                .nickname("nonExistentNickname")
                .password("$2a$10$G7E1tKKajd3S8/ORM17isOTCH0To0VkAnTjY7R4gkcgNpyLG/.tJC")
                .email("neUser1@mail.com")
                .roles(rolesneUser1)
                .build();
        neUser1.getRoles().forEach(role -> role.setUser(neUser1));

        userCollection.setNeUser1(neUser1);
        passwordCollection.put(neUser1, "password");

        userRepository.flush();
        return userCollection;
    }

}

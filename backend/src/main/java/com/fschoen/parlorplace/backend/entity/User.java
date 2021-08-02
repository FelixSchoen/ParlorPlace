package com.fschoen.parlorplace.backend.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_user_id")
    @SequenceGenerator(name = "seq_user_id", sequenceName = "seq_user_id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    @EqualsAndHashCode.Exclude
    @NotBlank
    @Size(min = 3, max = 15)
    private String username;

    @Column(nullable = false)
    @EqualsAndHashCode.Exclude
    @NotBlank
    @Size(min = 8, max = 255)
    private String password;

    @Column(nullable = false)
    @EqualsAndHashCode.Exclude
    @NotBlank
    @Size(min = 3, max = 15)
    private String nickname;

    @Column(nullable = false, unique = true)
    @EqualsAndHashCode.Exclude
    @NotBlank
    @Size(min = 3, max = 255)
    @Email
    private String email;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @NotNull
    private Set<Role> roles;

}

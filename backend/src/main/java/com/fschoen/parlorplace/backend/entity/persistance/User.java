package com.fschoen.parlorplace.backend.entity.persistance;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    @NotNull
    @Size(min = 3, max = 15)
    @NotBlank
    private String username;

    @Column(nullable = false)
    @NotNull
    @Size(min = 8, max = 255)
    @NotBlank
    private String password;

    @Column
    @NotNull
    @Size(min = 3, max = 15)
    @NotBlank
    private String nickname;

    @Column(unique = true)
    @NotNull
    @Size(min = 3, max = 255)
    @NotBlank
    @Email
    private String email;

    @OneToMany(mappedBy = "user")
    @NotNull
    private Set<Role> roles;

}

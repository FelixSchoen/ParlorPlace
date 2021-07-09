package com.fschoen.parlorplace.backend.entity.persistance;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank
    @Size(min = 3, max = 15)
    private String username;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 8, max = 255)
    private String password;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 3, max = 15)
    private String nickname;

    @Column(nullable = false, unique = true)
    @NotBlank
    @Size(min = 3, max = 255)
    @Email
    private String email;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @EqualsAndHashCode.Exclude
    @NotNull
    private Set<Role> roles;

}

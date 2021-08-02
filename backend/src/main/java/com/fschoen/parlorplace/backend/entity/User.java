package com.fschoen.parlorplace.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

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

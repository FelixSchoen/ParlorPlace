package com.fschoen.parlorplace.backend.entity.persistance;

import com.fschoen.parlorplace.backend.enumeration.UserRole;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Data
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_role_id")
    @SequenceGenerator(name = "seq_role_id", sequenceName = "seq_role_id")
    @EqualsAndHashCode.Exclude
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull
    private User user;

    @Column(nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole role;

}

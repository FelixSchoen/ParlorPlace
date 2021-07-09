package com.fschoen.parlorplace.backend.entity.persistance;

import com.fschoen.parlorplace.backend.enums.UserRole;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @NotNull
    private User user;

    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole role;

}

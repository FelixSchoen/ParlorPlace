package com.fschoen.parlorplace.backend.entity;

import com.fschoen.parlorplace.backend.enumeration.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
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

package com.fschoen.parlorplace.backend.security;

import com.fasterxml.jackson.annotation.*;
import com.fschoen.parlorplace.backend.entity.User;
import lombok.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.userdetails.*;

import javax.validation.constraints.*;
import java.util.*;
import java.util.stream.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserDetailsImplementation implements UserDetails {

    @NotNull
    private Long id;

    @NotBlank
    @Size(min = 3, max = 15)
    private String username;

    @JsonIgnore
    private String password;

    @NotBlank
    @Size(min = 3, max = 15)
    private String nickname;

    @NotBlank
    @Size(min = 3, max = 255)
    @Email
    private String email;

    @NotNull
    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImplementation fromUser(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().name()))
                .collect(Collectors.toList());

        return UserDetailsImplementation.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .authorities(authorities)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImplementation user = (UserDetailsImplementation) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

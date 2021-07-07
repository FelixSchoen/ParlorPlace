package com.fschoen.parlorplace.backend.controller.dto.user;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
@Data
public class UserDTO {

    private Long id;
    private String username;
    private String nickname;
    private String email;

}

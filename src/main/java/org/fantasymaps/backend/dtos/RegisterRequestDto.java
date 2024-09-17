package org.fantasymaps.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RegisterRequestDto {
    private String username;
    private String password;
    private Role role;
}

package org.fantasymaps.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthRequestDto {
    private String username;
    private String password;
}

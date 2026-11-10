package com.p3319.lab2.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "User.username can't be blank")
    private String username;

    @NotBlank(message = "User.password can't be blank")
    private String password;
}

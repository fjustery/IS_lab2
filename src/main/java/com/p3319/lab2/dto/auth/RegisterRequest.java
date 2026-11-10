package com.p3319.lab2.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "User.username can't be blank")
    @Size(min = 3, max = 50, message = "User.username length must be between 3 and 50")
    private String username;

    @NotBlank(message = "User.password can't be blank")
    @Size(min = 4, max = 100, message = "User.password length must be between 4 and 100")
    private String password;
}

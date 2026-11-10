package com.p3319.lab2.controller;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.p3319.lab2.dto.auth.AuthResponse;
import com.p3319.lab2.dto.auth.LoginRequest;
import com.p3319.lab2.dto.auth.LogoutRequest;
import com.p3319.lab2.dto.auth.RefreshRequest;
import com.p3319.lab2.dto.auth.RefreshTokenResponse;
import com.p3319.lab2.dto.auth.RegisterRequest;
import com.p3319.lab2.dto.auth.UserResponse;
import com.p3319.lab2.entity.User;
import com.p3319.lab2.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletResponse response) {

        AuthService.AuthResult result = authService.register(request);
        addAccessTokenCookie(response, result.accessToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(result.response());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {

        AuthService.AuthResult result = authService.login(request);
        addAccessTokenCookie(response, result.accessToken());
        return ResponseEntity.ok(result.response());
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(
            @Valid @RequestBody RefreshRequest request,
            HttpServletResponse response) {

        AuthService.RefreshResult result = authService.refresh(request.getRefreshToken());
        addAccessTokenCookie(response, result.newAccessToken());
        return ResponseEntity.ok(new RefreshTokenResponse(result.newRefreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestBody(required = false) LogoutRequest request,
            HttpServletResponse response) {

        String refreshToken = request != null ? request.getRefreshToken() : null;
        authService.logout(refreshToken);
        clearAccessTokenCookie(response);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(authService.getCurrentUser(user));
    }

    private void addAccessTokenCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from("accessToken", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Strict")
                .maxAge(Duration.ofMinutes(15))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private void clearAccessTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Strict")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}

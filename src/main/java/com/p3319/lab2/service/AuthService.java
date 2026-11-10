package com.p3319.lab2.service;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.p3319.lab2.dto.auth.AuthResponse;
import com.p3319.lab2.dto.auth.LoginRequest;
import com.p3319.lab2.dto.auth.RegisterRequest;
import com.p3319.lab2.dto.auth.UserResponse;
import com.p3319.lab2.entity.RefreshToken;
import com.p3319.lab2.entity.User;
import com.p3319.lab2.exception.ConflictException;
import com.p3319.lab2.exception.UnauthorizedException;
import com.p3319.lab2.repository.RefreshTokenRepository;
import com.p3319.lab2.repository.UserRepository;
import com.p3319.lab2.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${jwt.refresh-token-expiration-days:7}")
    private long refreshTokenExpirationDays;

    public record AuthResult(AuthResponse response, String accessToken) {}

    public record RefreshResult(String newRefreshToken, String newAccessToken) {}

    @Transactional
    public AuthResult register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user = userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user.getId(), user.getUsername());
        String refreshTokenStr = createRefreshToken(user);

        UserResponse userResponse = new UserResponse(user.getId(), user.getUsername());
        AuthResponse authResponse = new AuthResponse(userResponse, refreshTokenStr);

        return new AuthResult(authResponse, accessToken);
    }

    @Transactional
    public AuthResult login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername().trim())
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid username or password");
        }

        refreshTokenRepository.deleteByUser(user);

        String accessToken = jwtService.generateAccessToken(user.getId(), user.getUsername());
        String refreshTokenStr = createRefreshToken(user);

        UserResponse userResponse = new UserResponse(user.getId(), user.getUsername());
        AuthResponse authResponse = new AuthResponse(userResponse, refreshTokenStr);

        return new AuthResult(authResponse, accessToken);
    }

    @Transactional
    public RefreshResult refresh(String refreshTokenStr) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshTokenStr)
                .orElseThrow(() -> new UnauthorizedException("Invalid or expired refresh token"));

        if (token.getExpiryDate().isBefore(ZonedDateTime.now())) {
            refreshTokenRepository.delete(token);
            throw new UnauthorizedException("Invalid or expired refresh token");
        }

        User user = token.getUser();
        refreshTokenRepository.delete(token);

        String newAccessToken = jwtService.generateAccessToken(user.getId(), user.getUsername());
        String newRefreshTokenStr = createRefreshToken(user);

        return new RefreshResult(newRefreshTokenStr, newAccessToken);
    }

    @Transactional
    public void logout(String refreshTokenStr) {
        if (refreshTokenStr != null && !refreshTokenStr.isBlank()) {
            refreshTokenRepository.deleteByToken(refreshTokenStr);
        }
    }

    public UserResponse getCurrentUser(User user) {
        if (user == null) {
            throw new UnauthorizedException("Unauthorized");
        }
        return new UserResponse(user.getId(), user.getUsername());
    }

    private String createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(ZonedDateTime.now().plusDays(refreshTokenExpirationDays));
        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }
}

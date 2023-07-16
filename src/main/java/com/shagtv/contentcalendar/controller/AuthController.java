package com.shagtv.contentcalendar.controller;

import com.shagtv.contentcalendar.dto.LoginRequest;
import com.shagtv.contentcalendar.dto.RefreshTokenRequest;
import com.shagtv.contentcalendar.dto.TokerResponce;
import com.shagtv.contentcalendar.model.RefreshToken;
import com.shagtv.contentcalendar.model.User;
import com.shagtv.contentcalendar.model.UserRoles;
import com.shagtv.contentcalendar.repository.UserRepository;
import com.shagtv.contentcalendar.service.RefreshTokenService;
import com.shagtv.contentcalendar.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository repository;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    public AuthController(UserRepository repository, TokenService tokenService, AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService) {
        this.repository = repository;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        if (repository.findByName(user.getName()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }

        repository.save(user);
        return user;
    }

    @PostMapping("/token")
    public TokerResponce token(@RequestBody LoginRequest userLogin) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.name(), userLogin.password()));
        String token = tokenService.generateToken(authenticate);
        String refreshToken = refreshTokenService.createRefreshToken(userLogin.name()).getToken();
        String[] roles = authenticate.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new);
        return new TokerResponce(token, refreshToken, (String[])roles);
    }

    @PostMapping("/refreshToken")
    public TokerResponce refreshToken(@RequestBody RefreshTokenRequest token) {
        return refreshTokenService.findByToken(token.token())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword()));
                    String accessToken = tokenService.generateToken(authenticate);
                    String[] roles = user.getRoles().stream()
                            .map(UserRoles::toString)
                            .toArray(String[]::new);
                    return new TokerResponce(accessToken, token.token(), roles);
                }).orElseThrow(() -> new RuntimeException("Refresh token is not in database"));
    }
}

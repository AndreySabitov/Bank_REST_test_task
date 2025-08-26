package com.example.bankcards.service.auth;

import com.example.bankcards.dto.auth.SignInRequest;
import com.example.bankcards.dto.auth.TokenResponse;
import com.example.bankcards.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * Сервис аутентификации пользователей
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtTokenService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Аутентификация пользователя
     *
     * @param signInRequest данные для аутентификации пользователя
     * @return {@link TokenResponse}, который содержит JWT - токен
     */
    public TokenResponse signIn(SignInRequest signInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInRequest.getUsername(),
                signInRequest.getPassword()
        ));

        var user = userService.loadUserByUsername(signInRequest.getUsername());

        var jwt = jwtService.createToken(user);
        return new TokenResponse(jwt);
    }
}

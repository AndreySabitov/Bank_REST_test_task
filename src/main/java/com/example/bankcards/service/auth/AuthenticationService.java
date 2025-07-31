package com.example.bankcards.service.auth;

import com.example.bankcards.dto.auth.SignInRequest;
import com.example.bankcards.dto.auth.TokenResponse;
import com.example.bankcards.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtTokenService jwtService;
    private final AuthenticationManager authenticationManager;

    public TokenResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        var jwt = jwtService.createToken(user);
        return new TokenResponse(jwt);
    }
}

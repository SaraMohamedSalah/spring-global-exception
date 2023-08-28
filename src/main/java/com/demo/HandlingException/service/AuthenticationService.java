package com.demo.HandlingException.service;

import com.demo.HandlingException.config.CustomUserDetailsService;
import com.demo.HandlingException.config.JwtUtils;
import com.demo.HandlingException.enums.ERole;
import com.demo.HandlingException.exceptionhandler.exception.UserException;
import com.demo.HandlingException.exceptionhandler.exception.UserNonAuthorizedException;
import com.demo.HandlingException.models.AuthenticationRequest;
import com.demo.HandlingException.models.AuthenticationResponse;
import com.demo.HandlingException.models.RegisterRequest;
import com.demo.HandlingException.repository.Token;
import com.demo.HandlingException.repository.TokenRepository;
import com.demo.HandlingException.repository.User;
import com.demo.HandlingException.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        String jwtToken = this.jwtUtils.generateTokenFromUsername(request.getEmail());
        return AuthenticationResponse.builder().accessToken(jwtToken).build();
    }

    public Optional<AuthenticationResponse> refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String refreshToken = authHeader.substring(7);
            String userEmail = this.jwtUtils.getUserNameFromJwtToken(refreshToken);
            if (userEmail != null) {
                UserDetails user = this.customUserDetailsService.loadUserByUsername(userEmail);
                if (this.jwtUtils.validateJwtToken(refreshToken, user.getUsername())) {
                    String accessToken = this.jwtUtils.generateTokenFromUsername(user.getUsername());
                    return Optional.of(AuthenticationResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build());
                }
            }

            return Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder().email(request.getEmail()).password(this.passwordEncoder.encode(request.getPassword())).role(request.getRole()).build();
        User savedUser = (User)this.userRepository.save(user);
        String jwtToken = this.jwtUtils.generateTokenFromUsername(user.getUsername());
        String refreshToken = this.jwtUtils.generateTokenFromUsername(user.getUsername());
        this.saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder().user(user).token(jwtToken).expired(false).build();
        this.tokenRepository.save(token);
    }

    public AuthenticationResponse getAuthenticatedUser(AuthenticationRequest request) {
        User user = (User)this.userRepository.findByEmail(request.getEmail()).orElseThrow(() -> {
            return new UserException("user email not found - " + request.getEmail());
        });
        String jwtToken = this.jwtUtils.generateTokenFromUsername(request.getEmail());
        return AuthenticationResponse.builder().accessToken(jwtToken).email(user.getEmail()).build();
    }

    public AuthenticationResponse getAuthorizedUser(AuthenticationRequest request) {
        User user = (User)this.userRepository.findByEmailAndRole(request.getEmail(), ERole.valueOf(request.getRole())).orElseThrow(() -> {
            return new UserNonAuthorizedException("user email not authorized - " + request.getEmail());
        });
        String jwtToken = this.jwtUtils.generateTokenFromUsername(request.getEmail());
        return AuthenticationResponse.builder().accessToken(jwtToken).email(user.getEmail()).build();
    }

}

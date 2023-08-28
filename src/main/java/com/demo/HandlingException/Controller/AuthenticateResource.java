package com.demo.HandlingException.Controller;

import com.demo.HandlingException.models.AuthenticationRequest;
import com.demo.HandlingException.models.AuthenticationResponse;
import com.demo.HandlingException.models.RegisterRequest;
import com.demo.HandlingException.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/auth"})
public class AuthenticateResource {
    @Autowired
    private AuthenticationService authenticationService;

    public AuthenticateResource() {
    }

    @PostMapping({"/authenticate"})
    public String authenticateAndGetToken(@RequestBody AuthenticationRequest request) {
        AuthenticationRequest user = AuthenticationRequest.builder().email(request.getEmail()).password(request.getPassword()).build();
        return this.authenticationService.authenticate(user).getAccessToken();
    }

    @PostMapping({"/refresh-token"})
    public AuthenticationResponse refreshToken(HttpServletRequest request) throws UsernameNotFoundException {
        return (AuthenticationResponse)this.authenticationService.refreshToken(request).orElseThrow(() -> {
            return new UsernameNotFoundException("Username not found");
        });
    }

    @PostMapping({"/register"})
    public AuthenticationResponse register(@RequestBody RegisterRequest request) throws UsernameNotFoundException {
        return this.authenticationService.register(request);
    }

    @GetMapping
    public AuthenticationResponse getAuthenticatedUser(@RequestBody AuthenticationRequest request) {
        AuthenticationRequest user = AuthenticationRequest.builder().email(request.getEmail()).password(request.getPassword()).build();
        return this.authenticationService.getAuthenticatedUser(user);
    }

    @GetMapping({"/authorize"})
    public AuthenticationResponse getAuthorizedUser(@RequestBody AuthenticationRequest request) {
        AuthenticationRequest user = AuthenticationRequest.builder().email(request.getEmail()).password(request.getPassword()).role(request.getRole()).build();
        return this.authenticationService.getAuthorizedUser(user);
    }
}

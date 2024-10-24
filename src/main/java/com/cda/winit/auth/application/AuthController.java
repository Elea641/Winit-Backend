package com.cda.winit.auth.application;

import com.cda.winit.auth.domain.dto.AuthRequest;
import com.cda.winit.auth.domain.dto.AuthResponse;
import com.cda.winit.auth.domain.dto.RegisterRequest;
import com.cda.winit.auth.domain.service.AuthService;
import com.cda.winit.auth.domain.service.PasswordForgottenService;
import com.cda.winit.auth.infrastructure.exception.AuthentificationServiceException;
import com.cda.winit.auth.infrastructure.exception.PasswordForgottenErrorException;
import com.cda.winit.user.domain.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final PasswordForgottenService passwordForgottenService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest request, HttpServletRequest httpRequest) throws Exception {
        return ResponseEntity.ok(authService.register(request, httpRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest request, HttpServletRequest httpRequest) {
        try {
            AuthResponse authenticationResponse = authService.authenticate(request, httpRequest);
            return ResponseEntity.ok(authenticationResponse);
        } catch (AuthentificationServiceException ex) {
            return ResponseEntity.badRequest().body("Votre compte a été désactivé");
        }
    }

    @PostMapping("/password-forgotten/{email}")
    public ResponseEntity<?> passwordForgotten(@PathVariable String email) throws PasswordForgottenErrorException {
        try {
            passwordForgottenService.tokenGenerator(email);
            return ResponseEntity.status(201).body(Collections.singletonMap("message", "Si un compte correspond à cette adresse mail, vous allez recevoir un e-mail pour modifier votre mot de passe"));
        } catch (Exception e) {
            throw new PasswordForgottenErrorException(e.getMessage());
        }
    }

    @PostMapping("/new-password/{token}")
    public ResponseEntity<?> newPassword(@PathVariable String token, @RequestBody User userBody) throws PasswordForgottenErrorException {
        try {
            passwordForgottenService.checkTokenValidityAndCreateNewPassword(token, userBody);
            return ResponseEntity.status(200).body(Collections.singletonMap("message", "Mot de passe modifié avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}


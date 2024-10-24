package com.cda.winit.auth.domain.service;

import com.cda.winit.auth.domain.dto.AuthRequest;
import com.cda.winit.auth.domain.dto.AuthResponse;
import com.cda.winit.auth.domain.dto.RegisterRequest;
import com.cda.winit.auth.domain.service.interfaces.IAuthService;
import com.cda.winit.auth.infrastructure.exception.AuthentificationServiceException;
import com.cda.winit.user.domain.entity.User;
import com.cda.winit.auth.domain.service.util.JwtService;
import com.cda.winit.user.infrastructure.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public Map<String, String> register(RegisterRequest request, HttpServletRequest httpRequest) throws Exception {
        if (!repository.findByEmail(request.getEmail()).isPresent()) {
            var user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .city(request.getCity())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role("ROLE_USER")
                    .createdAt(request.getCreatedAt())
                    .isAcceptTerms(request.getAcceptTerms())
                    .build();

            repository.save(user);

            Map<String, String> body = new HashMap<>();
            body.put("message", "Account successfully created");
            return body;

        } else {
            httpRequest.setAttribute("username_taken_exception", "Username already taken");
            throw new Exception("Username already taken");
        }
    }

    public AuthResponse authenticate(AuthRequest request, HttpServletRequest httpRequest) {

        /*
         * Compare request password and database password and
         * send user to spring context
         * if no user found throw exception
         */
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            User user = repository.findByEmail(request.getEmail()).orElseThrow();

            if(!user.getIsAcceptTerms()) {
                throw new AuthentificationServiceException("Votre compte a été désactivé");
            }
                /* Extract user role */
                Map<String, Object> extraClaims = new HashMap<>();
                extraClaims.put("role", user.getRole());

                /* Generate token with role */
                String jwtToken = jwtService.generateToken(new HashMap<>(extraClaims), user);
                return AuthResponse.builder()
                        .token(jwtToken)
                        .build();
        } catch (BadCredentialsException ex) {
            httpRequest.setAttribute("bad_credentials", ex.getMessage());
            throw new BadCredentialsException("Bad credentials");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
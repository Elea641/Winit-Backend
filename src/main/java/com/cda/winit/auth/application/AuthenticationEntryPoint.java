package com.cda.winit.auth.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class AuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {

    // This class sends authentication errors to client
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        response.setStatus(UNAUTHORIZED.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setHeader("error", exception.getMessage());

        Map<String, String> error = new HashMap<>();

        if (request.getAttribute("expired_exception") != null) {
            error.put("is_token_expired", "true");
            error.put("error_message", "JWT has expired. Please log in again.");
        } else if (request.getAttribute("malformed_exception") != null) {
            error.put("is_jwt_malformed", "true");
            error.put("error_message", "JWT is malformed. Please verify its integrity.");
        } else if (request.getAttribute("jwt_exception") != null) {
            error.put("is_jwt_exception", "true");
            error.put("error_message", "");
        } else if (request.getAttribute("username_taken_exception") != null) {
            error.put("is_username_taken", "true");
            error.put("error_message", "Email already taken.");
        } else if (request.getAttribute("bad_credentials") != null) {
            error.put("bad_credentials", "true");
            error.put("error_message", "Account does not exist OR you used an incorrect pair of email/mdp.");
        } else if (request.getAttribute("no_jwt_provided") != null) {
            error.put("no_jwt_provided", "true");
            error.put("error_message", "No JWT provided.");
        }

        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }
}


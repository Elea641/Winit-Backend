package com.cda.winit.auth.domain.service.interfaces;

import com.cda.winit.auth.domain.dto.AuthRequest;
import com.cda.winit.auth.domain.dto.AuthResponse;
import com.cda.winit.auth.domain.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface IAuthService {

    Map<String, String> register(RegisterRequest request, HttpServletRequest httpRequest) throws Exception;

    AuthResponse authenticate(AuthRequest request, HttpServletRequest httpRequest);


}
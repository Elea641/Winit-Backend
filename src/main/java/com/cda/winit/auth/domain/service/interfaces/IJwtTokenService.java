package com.cda.winit.auth.domain.service.interfaces;

import io.jsonwebtoken.Claims;
import java.util.Date;
import java.util.function.Function;

public interface IJwtTokenService {

    String getUsernameFromToken(String token);

    <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver);

    Date extractExpiration(String token);
}

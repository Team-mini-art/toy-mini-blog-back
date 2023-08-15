package study.till.back.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import study.till.back.config.jwt.JwtTokenProvider;
import study.till.back.dto.token.TokenRequest;
import study.till.back.dto.token.TokenResponse;
import study.till.back.exception.token.ExpiredRefreshTokenException;
import study.till.back.exception.token.UnauthorizedTokenException;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;

    public ResponseEntity<TokenResponse> refreshToken(TokenRequest tokenRequest) {
        String refreshToken = tokenRequest.getRefreshToken();
        if (jwtTokenProvider.validateToken(refreshToken)) {
            Claims claims = jwtTokenProvider.parseClaims(refreshToken);
            String newAccessToken = jwtTokenProvider.createAccessToken(claims);

            TokenResponse tokenResponse = TokenResponse.builder()
                    .newAccessToken(newAccessToken)
                    .build();
            return ResponseEntity.ok(tokenResponse);
        }
        else {
            throw new ExpiredRefreshTokenException();
        }
    }
}

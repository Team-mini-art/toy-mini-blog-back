package study.till.back.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import study.till.back.config.jwt.JwtTokenProvider;
import study.till.back.dto.token.TokenInfo;
import study.till.back.dto.token.TokenRequest;
import study.till.back.dto.token.TokenResponse;
import study.till.back.exception.redis.NotEqualsRedisException;
import study.till.back.exception.redis.NotFoundRedisException;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    public ResponseEntity<TokenResponse> refreshToken(TokenRequest tokenRequest) {
        String email = tokenRequest.getEmail();
        String refreshToken = tokenRequest.getRefreshToken();

        ValueOperations<String, String> stringValueOperations = redisTemplate.opsForValue();
        String redisValue = stringValueOperations.get(email);

        jwtTokenProvider.validateToken(refreshToken);

        if (redisValue == null) {
            throw new NotFoundRedisException();
        }

        if (!redisValue.equals(refreshToken)) {
            throw new NotEqualsRedisException();
        }

        Claims claims = jwtTokenProvider.parseClaims(refreshToken);
        String newAccessToken = jwtTokenProvider.createAccessToken(claims);

        TokenResponse tokenResponse = TokenResponse.builder()
                .newAccessToken(newAccessToken)
                .build();
        return ResponseEntity.ok(tokenResponse);
    }

    public ResponseEntity<TokenInfo> createSuperToken() {
        return ResponseEntity.ok(jwtTokenProvider.createSuperToken());
    }
}

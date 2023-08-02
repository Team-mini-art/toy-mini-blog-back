package study.till.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import study.till.back.config.jwt.JwtTokenProvider;
import study.till.back.dto.TokenDTO;
import study.till.back.exception.token.UnauthorizedTokenException;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;

    public ResponseEntity<TokenDTO> refreshToken(TokenDTO tokenRequest) {
        String refreshToken = tokenRequest.getRefreshToken();
        if (jwtTokenProvider.validateToken(refreshToken)) {
            String subject = jwtTokenProvider.parseClaims(refreshToken).getSubject();
            String newAccessToken = jwtTokenProvider.createAccessToken(subject);

            TokenDTO tokenDTO = TokenDTO.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(refreshToken)
                    .build();
            return ResponseEntity.ok(tokenDTO);
        }
        else {
            throw new UnauthorizedTokenException();
        }
    }
}

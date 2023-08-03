package study.till.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.till.back.dto.token.TokenRequest;
import study.till.back.dto.token.TokenResponse;
import study.till.back.service.TokenService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TokenController {
    private final TokenService tokenService;

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody TokenRequest tokenRequest) {
        return tokenService.refreshToken(tokenRequest);
    }
}

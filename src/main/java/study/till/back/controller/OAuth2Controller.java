package study.till.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.till.back.dto.CommonResponse;
import study.till.back.dto.member.LoginResponse;
import study.till.back.service.OAuth2Service;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;

    @GetMapping("/success")
    public ResponseEntity<LoginResponse> oauth2Success(
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
            @AuthenticationPrincipal OAuth2User oAuth2User) {
        return oAuth2Service.loginSuccess(authorizedClient, oAuth2User);
    }

    @GetMapping("/fail")
    public ResponseEntity<CommonResponse> oauth2Fail() {
        return oAuth2Service.loginFail();
    }
}


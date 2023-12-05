package study.till.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import study.till.back.dto.CommonResponse;
import study.till.back.dto.member.LoginResponse;
import study.till.back.service.OAuth2Service;

@Controller
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;

    @GetMapping("/success")
    public ResponseEntity<LoginResponse> oauth2Success(@AuthenticationPrincipal OAuth2User oAuth2User) {
        return oAuth2Service.loginSuccess(oAuth2User);
    }

    @GetMapping("/fail")
    public ResponseEntity<CommonResponse> oauth2Fail() {
        return oAuth2Service.loginFail();
    }
}


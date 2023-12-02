package study.till.back.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import study.till.back.dto.member.LoginResponse;

@Controller
@RequestMapping("/oauth")
public class OAuthController {

    @RequestMapping("/success")
    public ResponseEntity<LoginResponse> oauthSuccess(@AuthenticationPrincipal OAuth2User principal) {
        System.out.println(principal);
        LoginResponse loginResponse = new LoginResponse();
        return ResponseEntity.ok(loginResponse);
    }
}

package study.till.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.till.back.dto.CommonResponse;
import study.till.back.dto.email.SendCodeRequest;
import study.till.back.dto.email.VerifyCodeRequset;
import study.till.back.service.EmailService;
import study.till.back.service.SESEmailService;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
@Profile("!local")
public class EmailController {

    private final EmailService emailService;
    private final SESEmailService sesEmailService;

    @PostMapping("/send-code")
    public ResponseEntity<CommonResponse> sendCode(@RequestBody SendCodeRequest SendCodeRequest) {
        return sesEmailService.sendCode(SendCodeRequest);
    }

    @PostMapping("/verify-code")
    public ResponseEntity<CommonResponse> verifyCode(@RequestBody VerifyCodeRequset verifyCodeRequset) {
        return emailService.verifyCode(verifyCodeRequset);
    }
}

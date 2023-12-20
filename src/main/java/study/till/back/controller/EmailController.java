package study.till.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.till.back.dto.CommonResponse;
import study.till.back.dto.email.SendCodeRequest;
import study.till.back.dto.email.VerifyCodeRequset;
import study.till.back.service.EmailService;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send-code")
    public ResponseEntity<CommonResponse> sendCode(@RequestBody SendCodeRequest SendCodeRequest) {
        return emailService.sendCode(SendCodeRequest);
    }

    @PostMapping("/verify-code")
    public ResponseEntity<CommonResponse> verifyCode(@RequestBody VerifyCodeRequset verifyCodeRequset) {
        return emailService.verifyCode(verifyCodeRequset);
    }
}

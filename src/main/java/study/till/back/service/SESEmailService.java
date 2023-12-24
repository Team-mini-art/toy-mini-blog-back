package study.till.back.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import study.till.back.dto.CommonResponse;
import study.till.back.dto.email.SendCodeRequest;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class SESEmailService {
    private final AmazonSimpleEmailService ses;
    private final EmailService emailService;
    private final RedisTemplate<String, String> redisTemplate;

    @Value(("${spring.mail.auth-code-expiration-millis}"))
    private long authCodeExpirationMillis;

    @Value(("${ses.mail}"))
    private String from;
    private final String AUTH_CODE_PREFIX = "AuthCode_";

    public ResponseEntity<CommonResponse> sendCode(SendCodeRequest SendCodeRequest) {
        String toEmail = SendCodeRequest.getToEmail();
        String title = "Mini Art Blog 이메일 인증 번호";
        String authCode = emailService.createCode();
        this.sendEmail(toEmail, title, authCode);
        redisTemplate.opsForValue().set(
                AUTH_CODE_PREFIX + toEmail,
                authCode,
                authCodeExpirationMillis,
                TimeUnit.MILLISECONDS
        );

        CommonResponse commonResponse = CommonResponse.createCommonResponse("SUCCESS",  toEmail + "로 인증코드를 정상적으로 전송하였습니다.");
        return ResponseEntity.ok(commonResponse);
    }

    public void sendEmail(String toEmail,String title,String authCode) {
        String text = emailService.makeText(authCode);

        SendEmailRequest request = new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(toEmail))
                .withMessage(new Message()
                        .withSubject(new Content()
                                .withCharset("UTF-8").withData(title)
                        )
                        .withBody(new Body()
                                .withHtml(new Content()
                                        .withCharset("UTF-8").withData(text)
                                )
                        )
                )
                .withSource(from);

        try {
            ses.sendEmail(request);
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

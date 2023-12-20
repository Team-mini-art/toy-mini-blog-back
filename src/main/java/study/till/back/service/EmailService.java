package study.till.back.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import study.till.back.dto.CommonResponse;
import study.till.back.dto.email.SendCodeRequest;
import study.till.back.dto.email.VerifyCodeRequset;
import study.till.back.exception.redis.NotEqualsRedisException;

import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;
    private final RedisTemplate<String, String> redisTemplate;

    @Value(("${spring.mail.auth-code-expiration-millis}"))
    private long authCodeExpirationMillis;
    private final String AUTH_CODE_PREFIX = "AuthCode";

    public void sendEmail(String toEmail, String title, String text) {
        SimpleMailMessage emailForm = createEmailForm(toEmail, title, text);

        try {
            emailSender.send(emailForm);
        } catch (RuntimeException e) {
            log.debug("MailService.sendEmail exception occur toEmail: {}, title: {}, text: {}", toEmail, title, text);
        }
    }

    private SimpleMailMessage createEmailForm(String toEmail, String title, String authCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(authCode);

        return message;
    }

    public String createCode() {
        int lenth = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("MemberService.createCode() exception occur");
            throw new RuntimeException();
        }
    }

    public ResponseEntity<CommonResponse> sendCode(SendCodeRequest SendCodeRequest) {
        String toEmail = SendCodeRequest.getToEmail();
        String title = "Mini Art Blog 이메일 인증 번호";
        String authCode = this.createCode();
        this.sendEmail(toEmail, title, authCode);
        redisTemplate.opsForValue().set(
                AUTH_CODE_PREFIX + toEmail,
                authCode,
                authCodeExpirationMillis,
                TimeUnit.MILLISECONDS
        );

        CommonResponse commonResponse = CommonResponse.builder()
                .status("SUCCESS")
                .message(toEmail + "로 인증코드를 정상적으로 전송하였습니다.")
                .build();

        return ResponseEntity.ok(commonResponse);
    }

    public ResponseEntity<CommonResponse> verifyCode(VerifyCodeRequset verifyCodeRequset) {
        String email = verifyCodeRequset.getFromEmail();
        String authCode = verifyCodeRequset.getAuthCode();
        ValueOperations<String, String> stringValueOperations = redisTemplate.opsForValue();
        String redisAuthCode = stringValueOperations.get(AUTH_CODE_PREFIX + email);

        if (!authCode.equals(redisAuthCode)) {
            throw new NotEqualsRedisException();
        }

        CommonResponse commonResponse = CommonResponse.builder()
                .status("SUCCESS")
                .message("인증코드가 일치합니다.")
                .build();

        return ResponseEntity.ok(commonResponse);
    }
}

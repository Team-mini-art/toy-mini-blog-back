package study.till.back.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
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
    private final SESEmailService sesEmailService;
    private final RedisTemplate<String, String> redisTemplate;

    @Value(("${spring.mail.auth-code-expiration-millis}"))
    private long authCodeExpirationMillis;
    private final String AUTH_CODE_PREFIX = "AuthCode_";

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

        CommonResponse commonResponse = CommonResponse.createCommonResponse("SUCCESS",  toEmail + "로 인증코드를 정상적으로 전송하였습니다.");
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

        CommonResponse commonResponse = CommonResponse.createCommonResponse("SUCCESS", "인증코드가 일치합니다.");
        return ResponseEntity.ok(commonResponse);
    }

    private void sendEmail(String toEmail, String title, String authCode) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(toEmail);
            messageHelper.setSubject(title);
            messageHelper.setText(makeText(authCode), true); // 두 번째 파라미터를 true로 설정하여 HTML 콘텐츠임을 나타냄
        };

        try {
            emailSender.send(messagePreparator);
        } catch (Exception e) {
            log.error("MailService.sendEmail exception occur toEmail: {}, title: {}, text: {}", toEmail, title, authCode);
            log.error(e.getMessage());
        }
    }
    private String createCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("EmailService.createCode() exception occur");
            throw new RuntimeException();
        }
    }

    private String makeText(String authCode) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style='width: 400px; margin: 0 auto;'>");
        sb.append("  <a href='http://mini-art-blog.com' rel='noreferrer noopener' target='_blank'>");
        sb.append("    <img src='https://mini-art-bucket.s3.ap-northeast-2.amazonaws.com/logo.png' style='display: block; width: 128px; margin: 0 auto;' loading='lazy'>");
        sb.append("  </a>");
        sb.append("");
        sb.append("  <div style='max-width: 100%; width: 400px; margin: 0 auto; padding: 1rem; text-align: justify; background: #f8f9fa; border: 1px solid #dee2e6; box-sizing: border-box; border-radius: 4px; color: #868e96; margin-top: 0.5rem; box-sizing: border-box;'>");
        sb.append("    <b style='black'>안녕하세요! </b>메일 인증을 계속하시려면 하단 인증번호를 입력해주세요.");
        sb.append("  </div>");
        sb.append("  <div style='text-decoration: none; text-align:center; display:block; margin: 0 auto; margin-top: 1rem; background: #845ef7; padding-top: 1rem; color: white; font-size: 1.25rem; padding-bottom: 1rem; font-weight: 600; border-radius: 4px;' rel='noreferrer noopener'>" + authCode + "</div>");
        sb.append("  ");
        sb.append("  <div style='text-align: center; margin-top: 1rem; color: #868e96; font-size: 0.85rem;'>");
        sb.append("    <div>이 코드는 10분간 유효합니다. </div>");
        sb.append("  </div>");
        sb.append("</div>");
        return sb.toString();
    }
}

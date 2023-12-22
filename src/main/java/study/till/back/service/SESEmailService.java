package study.till.back.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class SESEmailService {
    private final AmazonSimpleEmailService ses;

    public void sendEmail() {
        String from = "no-reply@mini-art-blog.com";
        String to = "pak4184@naver.com";
        String title = "Mini Art Blog 이메일 인증 번호 (AWS SES)";
//        String text = "content text";
        String authCode = this.createCode();
        String text = this.makeText(authCode);

        SendEmailRequest request = new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(to))
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

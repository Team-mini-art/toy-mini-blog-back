package study.till.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import study.till.back.config.jwt.JwtTokenProvider;
import study.till.back.dto.CommonResponse;
import study.till.back.dto.member.LoginResponse;
import study.till.back.dto.token.TokenInfo;
import study.till.back.entity.Member;
import study.till.back.entity.OAuthType;
import study.till.back.repository.MemberRepository;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2Service extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        return super.loadUser(userRequest);
    }

    public ResponseEntity<LoginResponse> loginSuccess(OAuth2AuthorizedClient authorizedClient, OAuth2User oAuth2User) {
        OAuthType loginOAuthType = OAuthType.valueOf(authorizedClient.getClientRegistration().getRegistrationId().toUpperCase());
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String nickname = (String) attributes.get("name");

        if (loginOAuthType == OAuthType.KAKAO) {
            email = (String) ((Map<String, Object>) attributes.get("kakao_account")).get("email");
            nickname = (String) ((Map<String, Object>) attributes.get("properties")).get("nickname");
        }
        else if (loginOAuthType == OAuthType.NAVER) {
            // NAVER 관련 코드
        }

        String finalEmail = email;
        String finalNickname = nickname;

        Member member = memberRepository.findById(email).orElseGet(() -> {
            Member newMember = Member.builder()
                    .email(finalEmail)
                    .nickname(finalNickname)
                    .oAuthType(loginOAuthType)
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();
            memberRepository.save(newMember);
            return newMember;
        });

        if (!member.getNickname().equals(nickname)) {
            member.updateMember(nickname);
            memberRepository.save(member);
        }

        TokenInfo tokenInfo = jwtTokenProvider.generateToken(member.getEmail(), member.getRoles());
        LoginResponse loginResponse = LoginResponse.builder()
                .status("SUCCESS")
                .message("로그인에 성공하였습니다.")
                .email(member.getEmail())
                .nickname(member.getNickname())
                .tokenInfo(tokenInfo)
                .build();
        return ResponseEntity.ok(loginResponse);
    }


    public ResponseEntity<CommonResponse> loginFail() {
        CommonResponse commonResponse = CommonResponse.builder()
                .status("FAIL")
                .message("로그인에 실패하였습니다.")
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(commonResponse);
    }
}

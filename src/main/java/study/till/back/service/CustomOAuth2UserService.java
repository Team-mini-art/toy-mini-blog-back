package study.till.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import study.till.back.config.jwt.JwtTokenProvider;
import study.till.back.dto.token.TokenInfo;
import study.till.back.entity.Member;
import study.till.back.entity.OAuthType;
import study.till.back.repository.MemberRepository;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String nickname = (String) attributes.get("name");

        Member member = memberRepository.findById(email).orElse(null);

        if (member != null) {
            TokenInfo tokenInfo = jwtTokenProvider.generateToken(member.getEmail(), member.getRoles());
            Map<String, Object> newAttributes = new HashMap<>(attributes);
            newAttributes.put("token", tokenInfo);
            return new DefaultOAuth2User(oAuth2User.getAuthorities(), newAttributes, "email");
        }

        Member newMember = Member.builder()
                .email(email)
                .nickname(nickname)
                .oAuthType(OAuthType.GOOGLE)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
        memberRepository.save(newMember);

        TokenInfo tokenInfo = jwtTokenProvider.generateToken(newMember.getEmail(), newMember.getRoles());

        Map<String, Object> newAttributes = new HashMap<>(attributes);
        newAttributes.put("token", tokenInfo);

        return new DefaultOAuth2User(oAuth2User.getAuthorities(), newAttributes, "email");
    }
}

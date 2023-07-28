package study.till.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import study.till.back.config.jwt.JwtTokenProvider;
import study.till.back.dto.LoginResponse;
import study.till.back.dto.LoginRequest;
import study.till.back.dto.TokenInfo;
import study.till.back.dto.FindMemberResponse;
import study.till.back.entity.Member;
import study.till.back.exception.NotFoundMemberException;
import study.till.back.repository.MemberRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Member signup(LoginRequest loginRequest) {
        loginRequest.setPassword(passwordEncoder.encode(loginRequest.getPassword()));
        Member members = Member.builder()
                .email(loginRequest.getEmail())
                .password(loginRequest.getPassword())
                .nickname(loginRequest.getNickname())
                .createdDate(LocalDateTime.now())
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

        memberRepository.save(members);
        return members;
    }

    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {

        Member member = memberRepository.findByEmail(loginRequest.getEmail());
        if (member == null) throw new NotFoundMemberException();
        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) throw new NotFoundMemberException();

        TokenInfo tokenInfo = jwtTokenProvider.generateToken(member.getId(), member.getRoles());
        LoginResponse loginResponse = LoginResponse.builder()
                .status("SUCCESS")
                .message("로그인 성공")
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .tokenInfo(tokenInfo)
                .build();
        return ResponseEntity.ok().body(loginResponse);
    }

    public List<FindMemberResponse> findMember() {
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(member -> FindMemberResponse.builder()
                        .id(member.getId())
                        .email(member.getEmail())
                        .nickname(member.getNickname())
                        .createdDate(member.getCreatedDate())
                        .build()
                )
                .collect(Collectors.toList());
    }
}

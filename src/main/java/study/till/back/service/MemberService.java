package study.till.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import study.till.back.config.jwt.JwtTokenProvider;
import study.till.back.dto.LoginResponse;
import study.till.back.dto.LoginRequest;
import study.till.back.dto.TokenInfo;
import study.till.back.dto.findMemberResponse;
import study.till.back.entity.Member;
import study.till.back.repository.MemberRepository;

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
        if (member == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        if (passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            TokenInfo tokenInfo = jwtTokenProvider.generateToken(member.getEmail(), member.getRoles());
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
        else {
            String status = "FAIL";
            String message = "로그인 실패, 아이디 또는 비밀번호를 확인해주세요.";
            LoginResponse loginResponse = LoginResponse.builder()
                    .status(status)
                    .message(message)
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse);
        }
    }

    public List<findMemberResponse> findMember() {
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(member -> findMemberResponse.builder()
                        .id(member.getId())
                        .email(member.getEmail())
                        .nickname(member.getNickname())
                        .createdDate(member.getCreatedDate())
                        .build()
                )
                .collect(Collectors.toList());
    }
}

package study.till.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import study.till.back.config.jwt.JwtTokenProvider;
import study.till.back.dto.*;
import study.till.back.entity.Member;
import study.till.back.exception.member.DuplicateMemberException;
import study.till.back.exception.member.InvalidPasswordException;
import study.till.back.exception.member.NotFoundMemberException;
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
    public ResponseEntity<CommonResponse> signup(SignupRequest signupRequest) {
        if (!isValidPassword(signupRequest.getPassword())) {
            throw new InvalidPasswordException();
        }

        Member member = memberRepository.findByEmail(signupRequest.getEmail());
        if (member != null) throw new DuplicateMemberException();

        signupRequest.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        Member members = Member.builder()
                .email(signupRequest.getEmail())
                .password(signupRequest.getPassword())
                .nickname(signupRequest.getNickname())
                .createdDate(LocalDateTime.now())
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

        memberRepository.save(members);
        CommonResponse commonResponse = CommonResponse.builder()
                .status("SUCCESS")
                .message("회원가입 완료")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
    }

    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {

        Member member = memberRepository.findByEmail(loginRequest.getEmail());
        if (member == null) throw new NotFoundMemberException();
        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) throw new NotFoundMemberException();

        TokenInfo tokenInfo = jwtTokenProvider.generateToken(member.getEmail(), member.getRoles());
        LoginResponse loginResponse = LoginResponse.builder()
                .status("SUCCESS")
                .message("로그인 성공")
                .tokenInfo(tokenInfo)
                .build();
        return ResponseEntity.ok().body(loginResponse);
    }

    public List<FindMemberResponse> findMember() {
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(member -> FindMemberResponse.builder()
                        .email(member.getEmail())
                        .nickname(member.getNickname())
                        .createdDate(member.getCreatedDate())
                        .build()
                )
                .collect(Collectors.toList());
    }

    public boolean isValidPassword(String password) {
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{10,}$";
        return password.matches(pattern);
    }
}

package study.till.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import study.till.back.config.jwt.JwtTokenProvider;
import study.till.back.dto.*;
import study.till.back.dto.member.*;
import study.till.back.dto.token.TokenInfo;
import study.till.back.entity.Member;
import study.till.back.exception.member.DuplicateMemberException;
import study.till.back.exception.member.InvalidEmailException;
import study.till.back.exception.member.InvalidPasswordException;
import study.till.back.exception.member.NotFoundMemberException;
import study.till.back.repository.MemberRepository;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public ResponseEntity<CommonResponse> signup(SignupRequest signupRequest) {
        if (!isValidEmail(signupRequest.getEmail())) {
            throw new InvalidEmailException();
        }

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
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

        memberRepository.save(members);
        CommonResponse commonResponse = CommonResponse.builder()
                .status("SUCCESS")
                .message("회원가입 완료되었습니다.")
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
                .message("로그인에 성공하였습니다.")
                .email(member.getEmail())
                .nickname(member.getNickname())
                .tokenInfo(tokenInfo)
                .build();
        return ResponseEntity.ok().body(loginResponse);
    }

    public FindMemberPageResponse findMembers(Pageable pageable) {
        Page<Member> contents = memberRepository.findAll(pageable);

        List<FindMemberResponse> members = contents.stream().map(FindMemberResponse::from).collect(Collectors.toList());

        FindMemberPageResponse findMemberPageResponse = FindMemberPageResponse.builder()
                .members(members)
                .totalElements(contents.getTotalElements())
                .totalPages(contents.getTotalPages())
                .pageNumber(contents.getNumber())
                .pageSize(contents.getSize())
                .hasPrevious(contents.hasPrevious())
                .hasNext(contents.hasNext())
                .build();

        return findMemberPageResponse;
    }

    public boolean isValidEmail(String email) {
        String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

        return EMAIL_PATTERN.matcher(email).matches();
    }

    public boolean isValidPassword(String password) {
        String pattern = "^(?=.*[!@#$%^&*()-=_+\\[\\]{}\\\\,/<>?'\":;|]).*(?=.*[A-Z]).*(?=.*[0-9]).{10,}$";
        return password.matches(pattern);
    }
}

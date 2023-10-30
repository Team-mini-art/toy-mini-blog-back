package study.till.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import study.till.back.config.jwt.JwtTokenProvider;
import study.till.back.dto.*;
import study.till.back.dto.file.UploadResult;
import study.till.back.dto.member.*;
import study.till.back.dto.token.TokenInfo;
import study.till.back.entity.Member;
import study.till.back.entity.MemberAttach;
import study.till.back.exception.common.NoDataException;
import study.till.back.exception.member.DuplicateMemberException;
import study.till.back.exception.member.InvalidEmailException;
import study.till.back.exception.member.InvalidPasswordException;
import study.till.back.exception.member.NotFoundMemberException;
import study.till.back.repository.MemberAttachRepository;
import study.till.back.repository.MemberRepository;
import study.till.back.util.FileUtil;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    @Value("${upload.path}")
    private String uploadPath;

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberAttachRepository memberAttachRepository;

    @Transactional
    public ResponseEntity<CommonResponse> signup(SignupRequest signupRequest) {
        if (!isValidEmail(signupRequest.getEmail())) {
            throw new InvalidEmailException();
        }

        if (!isValidPassword(signupRequest.getPassword())) {
            throw new InvalidPasswordException();
        }

        if (!memberRepository.findById(signupRequest.getEmail()).isEmpty()) {
            throw new DuplicateMemberException();
        }

        signupRequest.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        Member member = Member.builder()
                .email(signupRequest.getEmail())
                .password(signupRequest.getPassword())
                .nickname(signupRequest.getNickname())
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

        memberRepository.save(member);
        CommonResponse commonResponse = CommonResponse.builder()
                .status("SUCCESS")
                .message("회원가입 완료되었습니다.")
                .build();

        /**
         * 파일 업로드 구현
         * - 단일 파일 업로드 구현 후 다중 파일 업로드도 구현
         */
        if (!signupRequest.getMultipartFile().isEmpty()) {
            UploadResult uploadResult = FileUtil.uploadFile(uploadPath, signupRequest.getMultipartFile());

            boolean result = uploadResult.isResult();

            if (result) {
                MemberAttach memberAttach = MemberAttach.builder()
                        .originFileName(uploadResult.getOriginFileName())
                        .savedFileName(uploadResult.getSavedFileName())
                        .uploadDir(uploadResult.getUploadDir())
                        .extension(uploadResult.getExtension())
                        .size(uploadResult.getSize())
                        .contentType(uploadResult.getContentType())
                        .member(member)
                        .build();

                memberAttachRepository.save(memberAttach);
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
    }

    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {

        Member member = memberRepository.findById(loginRequest.getEmail()).orElseThrow(() -> new NotFoundMemberException());
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

    public ResponseEntity<FindMemberPageResponse> findMembers(Pageable pageable, String email, String nickname) {
        Specification<Member> spec = Specification.where(Specification.<Member>where(null));

        if (email != null && !email.isEmpty()) {
            spec = spec.and((root, query, builder) -> builder.like(root.get("email"), "%" + email + "%"));
        }

        if (nickname != null && !nickname.isEmpty()) {
            spec = spec.and((root, query, builder) -> builder.like(root.get("nickname"), "%" + nickname + "%"));
        }

        Page<Member> contents = memberRepository.findAll(spec, pageable);

        if (contents.isEmpty()) {
            throw new NoDataException();
        }

        List<FindMemberResponse> members = contents.stream().map(FindMemberResponse::from).collect(Collectors.toList());
        FindMemberPageResponse findMemberPageResponse = FindMemberPageResponse.from(contents, members);
        return ResponseEntity.ok().body(findMemberPageResponse);
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

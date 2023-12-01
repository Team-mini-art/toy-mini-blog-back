package study.till.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import study.till.back.config.jwt.JwtTokenProvider;
import study.till.back.dto.*;
import study.till.back.dto.file.FileUploadDTO;
import study.till.back.dto.member.*;
import study.till.back.dto.token.TokenInfo;
import study.till.back.entity.Member;
import study.till.back.entity.MemberAttach;
import study.till.back.entity.OAuthType;
import study.till.back.exception.common.NoDataException;
import study.till.back.exception.member.DuplicateMemberException;
import study.till.back.exception.member.InvalidEmailException;
import study.till.back.exception.member.InvalidPasswordException;
import study.till.back.exception.member.NotFoundMemberException;
import study.till.back.repository.MemberAttachRepository;
import study.till.back.repository.MemberRepository;
import study.till.back.util.valid.SignupValidUtil;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberAttachRepository memberAttachRepository;
    private final S3Service s3Service;

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
            throw new DuplicateMemberException();
        }

        Member newMember = Member.builder()
                .email(email)
                .nickname(nickname)
                .oAuthType(OAuthType.GOOGLE)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
        memberRepository.save(newMember);

        TokenInfo tokenInfo = jwtTokenProvider.generateToken(newMember.getEmail(), newMember.getRoles());

        return oAuth2User;
    }

    @Transactional
    public ResponseEntity<CommonResponse> signup(SignupRequest signupRequest) {
        if (!SignupValidUtil.isValidEmail(signupRequest.getEmail())) {
            throw new InvalidEmailException();
        }

        if (!SignupValidUtil.isValidPassword(signupRequest.getPassword())) {
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

        if (signupRequest.getMultipartFile() != null && !signupRequest.getMultipartFile().isEmpty()) {
            FileUploadDTO fileUploadDTO = s3Service.uploadFile(signupRequest.getMultipartFile());

            boolean result = fileUploadDTO.isResult();

            if (result) {
                MemberAttach memberAttach = MemberAttach.builder()
                        .originFileName(fileUploadDTO.getOriginFileName())
                        .savedFileName(fileUploadDTO.getSavedFileName())
                        .uploadDir(fileUploadDTO.getUploadDir())
                        .extension(fileUploadDTO.getExtension())
                        .size(fileUploadDTO.getSize())
                        .contentType(fileUploadDTO.getContentType())
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

    @Transactional
    public ResponseEntity<CommonResponse> updateMember(MemberRequest memberRequest) {
        Member member = memberRepository.findById(memberRequest.getEmail()).orElseThrow(NotFoundMemberException::new);
        member.updatePost(memberRequest.getNickname());
        memberRepository.save(member);

        FileUploadDTO fileUploadDTO = new FileUploadDTO();
        boolean uploaded = false;
        if (memberRequest.getMultipartFile() != null && !memberRequest.getMultipartFile().isEmpty()) {
            fileUploadDTO = s3Service.uploadFile(memberRequest.getMultipartFile());
            uploaded = fileUploadDTO.isResult();
        }

        if (uploaded) {
            List<MemberAttach> attachList = memberAttachRepository.findByMember_Email(memberRequest.getEmail());

            if (attachList != null && !attachList.isEmpty()) {
                for (MemberAttach memberAttach : attachList) {
                    boolean deleted = s3Service.deleteFile(memberAttach.getUploadDir() + memberAttach.getSavedFileName());

                    if (deleted) {
                        memberAttachRepository.delete(memberAttach);
                    }
                }
            }

            MemberAttach memberAttach = MemberAttach.builder()
                    .originFileName(fileUploadDTO.getOriginFileName())
                    .savedFileName(fileUploadDTO.getSavedFileName())
                    .uploadDir(fileUploadDTO.getUploadDir())
                    .extension(fileUploadDTO.getExtension())
                    .size(fileUploadDTO.getSize())
                    .contentType(fileUploadDTO.getContentType())
                    .member(member)
                    .build();

            memberAttachRepository.save(memberAttach);
        }

        CommonResponse commonResponse = CommonResponse.builder()
                .status("SUCCESS")
                .message("회원 정보가 수정되었습니다.")
                .build();
        return ResponseEntity.ok(commonResponse);
    }
}
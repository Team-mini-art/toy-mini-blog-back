package study.till.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import study.till.back.dto.LoginResponse;
import study.till.back.dto.MemberDto;
import study.till.back.entity.Member;
import study.till.back.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member signup(MemberDto memberDto) {
        Member members = memberDto.toEntity();
        memberRepository.save(members);
        return members;
    }

    public ResponseEntity<LoginResponse> login(MemberDto memberDto) {

        Member member = memberRepository.findByEmailAndPassword(memberDto.getEmail(), memberDto.getPassword());
        if (member != null) {
            LoginResponse loginResponse = LoginResponse.builder()
                    .status("SUCCESS")
                    .message("로그인 성공")
                    .id(member.getId())
                    .email(member.getEmail())
                    .nickname(member.getNickname())
                    .build();
            return ResponseEntity.ok().body(loginResponse);
        }
        else {
            LoginResponse loginResponse = LoginResponse.builder()
                    .status("FAIL")
                    .message("로그인 실패, 아이디 또는 비밀번호를 확인해주세요.")
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse);
        }
    }

    public List<MemberDto> findMember() {
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(member -> MemberDto.builder()
                        .id(member.getId())
                        .email(member.getEmail())
                        .password(member.getPassword())
                        .nickname(member.getNickname())
                        .createdDate(member.getCreatedDate())
                        .build()
                )
                .collect(Collectors.toList());
    }
}

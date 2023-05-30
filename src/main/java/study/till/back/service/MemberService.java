package study.till.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.till.back.dto.MemberDto;
import study.till.back.entity.Member;
import study.till.back.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

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

    public void signup(MemberDto memberDto) {
        Member members = new Member();

        members.setEmail(memberDto.getEmail());
        members.setPassword(memberDto.getPassword());
        members.setNickname(memberDto.getNickname());
        members.setCreatedDate(LocalDateTime.now());
        memberRepository.save(members);
    }
}

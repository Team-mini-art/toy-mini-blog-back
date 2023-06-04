package study.till.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    public Member login(MemberDto memberDto) {
        return memberRepository.findByEmailAndPassword(memberDto.getEmail(), memberDto.getPassword());
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

package study.till.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.till.back.dto.LoginResponse;
import study.till.back.dto.MemberDto;
import study.till.back.entity.Member;
import study.till.back.repository.MemberRepository;
import study.till.back.service.MemberService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @PostMapping("/signup")
    public Member signup(@RequestBody MemberDto memberDto) {
        return memberService.signup(memberDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody MemberDto memberDto) {
        return memberService.login(memberDto);
    }

    @GetMapping("/members")
    public List<MemberDto> findMember() {
        memberService.findMember();
        return memberService.findMember();
    }
}

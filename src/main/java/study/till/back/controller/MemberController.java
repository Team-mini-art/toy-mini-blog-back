package study.till.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.till.back.dto.LoginResponse;
import study.till.back.dto.LoginRequest;
import study.till.back.dto.findMemberResponse;
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
    public Member signup(@RequestBody LoginRequest loginRequest) {
        return memberService.signup(loginRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return memberService.login(loginRequest);
    }

    @GetMapping("/members")
    public List<findMemberResponse> findMember() {
        memberService.findMember();
        return memberService.findMember();
    }
}

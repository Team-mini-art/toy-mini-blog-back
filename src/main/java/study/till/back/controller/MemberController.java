package study.till.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.till.back.dto.*;
import study.till.back.dto.member.FindMemberResponse;
import study.till.back.dto.member.LoginRequest;
import study.till.back.dto.member.LoginResponse;
import study.till.back.dto.member.SignupRequest;
import study.till.back.service.MemberService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> signup(@RequestBody SignupRequest signupRequest) {
        return memberService.signup(signupRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return memberService.login(loginRequest);
    }

    @GetMapping("/members")
    public List<FindMemberResponse> findMember() {
        memberService.findMember();
        return memberService.findMember();
    }
}

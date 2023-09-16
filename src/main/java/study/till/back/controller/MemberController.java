package study.till.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public List<FindMemberResponse> findMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "email") String sort,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        return memberService.findMembers(pageable);
    }
}

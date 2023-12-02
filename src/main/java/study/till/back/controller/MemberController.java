package study.till.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import study.till.back.dto.*;
import study.till.back.dto.member.*;
import study.till.back.service.MemberService;
import study.till.back.service.S3Service;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    @PostMapping(value = "/signup", consumes = {"multipart/form-data"})
    public ResponseEntity<CommonResponse> signup(
            @Valid @RequestPart(value = "data") SignupRequest signupRequest,
            @RequestPart(value = "file", required = false) MultipartFile multipartFile) {
        signupRequest.setMultipartFile(multipartFile);
        return memberService.signup(signupRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return memberService.login(loginRequest);
    }

    @GetMapping("/members")
    public ResponseEntity<FindMemberPageResponse> findMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdDate") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String nickname
            ) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        return memberService.findMembers(pageable, email, nickname);
    }

    @PatchMapping(value = "/members", consumes = {"multipart/form-data"})
    public ResponseEntity<CommonResponse> updateMember(
            @RequestPart(value = "data") MemberRequest memberRequest,
            @RequestPart(value = "file", required = false) MultipartFile multipartFile) {
        memberRequest.setMultipartFile(multipartFile);
        return memberService.updateMember(memberRequest);
    }
}

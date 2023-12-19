package study.till.back.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.till.back.dto.email.EmailRequset;
import study.till.back.service.MemberService;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final MemberService memberService;

    @PostMapping("/sendAuthCode")
    public ResponseEntity sendMessage(@RequestBody EmailRequset emailRequset) {
        memberService.sendCodeEmail(emailRequset);
        return new ResponseEntity(HttpStatus.OK);
    }
}

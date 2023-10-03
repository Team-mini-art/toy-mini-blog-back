package study.till.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.till.back.dto.CreateCommonResponse;
import study.till.back.dto.reply.ReplyRequest;
import study.till.back.service.ReplyService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/reply")
    public ResponseEntity<CreateCommonResponse> createReplyComment(@RequestBody ReplyRequest replyRequest) {
        return replyService.createReplyComment(replyRequest);
    }
}

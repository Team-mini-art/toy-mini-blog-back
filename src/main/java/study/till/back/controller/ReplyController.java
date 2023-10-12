package study.till.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.till.back.dto.CommonResponse;
import study.till.back.dto.CreateCommonResponse;
import study.till.back.dto.reply.ReplyRequest;
import study.till.back.service.ReplyService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/replies")
    public ResponseEntity<CreateCommonResponse> createReply(@RequestBody ReplyRequest replyRequest) {
        return replyService.createReply(replyRequest);
    }

    @PutMapping("/replies/{id}")
    public ResponseEntity<CommonResponse> updateReply(@PathVariable("id") Long id, @RequestBody ReplyRequest replyRequest) {
        return replyService.updateReply(id, replyRequest);
    }

    @DeleteMapping("/replies/{id}")
    public ResponseEntity<CommonResponse> deleteReply(@PathVariable("id") Long id) {
        return replyService.deleteReply(id);
    }
}

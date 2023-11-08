package study.till.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.till.back.dto.CommonResponse;
import study.till.back.dto.CreateCommonResponse;
import study.till.back.dto.reply.ReplyRequest;
import study.till.back.service.ReplyService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/replies")
    public ResponseEntity<CreateCommonResponse> createReply(@Valid @RequestBody ReplyRequest replyRequest) {
        return replyService.createReply(replyRequest);
    }

    @PutMapping("/replies/{id}")
    public ResponseEntity<CommonResponse> updateReply(@PathVariable("id") Long id, @Valid @RequestBody ReplyRequest replyRequest) {
        return replyService.updateReply(id, replyRequest);
    }

    @DeleteMapping("/replies/{id}")
    public ResponseEntity<CommonResponse> deleteReply(@PathVariable("id") Long id) {
        return replyService.deleteReply(id);
    }
}

package com.noname.controller;

import com.noname.dto.ReplyDTO;
import com.noname.security.CustomUser;
import com.noname.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController // ajax 요청 처리 -> @ResponseBody + @Controller
@RequestMapping("/replies")
@RequiredArgsConstructor
public class ReplyController {

    final Long USER = 2L;

    private final ReplyService replyService;

    // 등록
    // 회원아이디 받아서 replyer에 넣어주는 처리 필요
    @PostMapping("/add")
    public ResponseEntity<String> addReply(@AuthenticationPrincipal CustomUser user, @ModelAttribute ReplyDTO replyDTO) {
    	
    	if(user == null) {
    		new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
        log.info("reply add replyForm : {}", replyDTO.toString());
        Long resultRid = replyService.addReply(user.getMember().getMemberId(), replyDTO);
        log.info("reply add resultRid : {}", resultRid);
        return resultRid != null ? new ResponseEntity<>("success", HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 댓글 목록 조회
    @GetMapping("/list/{productId}")
    public ResponseEntity<List<ReplyDTO>> replies(@PathVariable Long productId) {
        log.info("reply list productId : {}", productId);
        return new ResponseEntity<>(replyService.getAllReplies(productId), HttpStatus.OK);
    }

    // 특정 댓글 조회
    @GetMapping("/{rid}")
    public ResponseEntity<ReplyDTO> getOneReply(@PathVariable Long rid){
        log.info("get one reply rid : {}", rid);
        return new ResponseEntity<>(replyService.getOneReply(rid), HttpStatus.OK);
    }

    // 댓글 삭제
    @DeleteMapping("/{rid}")
    public ResponseEntity<String> deleteReply(@PathVariable Long rid){
        log.info("delete reply rid : {}", rid);
        replyService.deleteReply(rid);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }






}

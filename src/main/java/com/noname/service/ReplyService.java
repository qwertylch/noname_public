package com.noname.service;

import com.noname.dto.ReplyDTO;

import java.util.List;

public interface ReplyService {
    
	Long addReply(Long memberId, ReplyDTO replyDTO);
    
    List<ReplyDTO> getAllReplies(Long bid);
    
    ReplyDTO getOneReply(Long rid);
    
    void deleteReply(Long rid);
    
}

package com.noname.service;

import com.noname.dto.ReplyDTO;
import com.noname.entity.Member;
import com.noname.entity.Product;
import com.noname.entity.ProductReply;
import com.noname.repository.MemberRepository;
import com.noname.repository.ProductRepository;
import com.noname.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{

    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional
    @Override
    public Long addReply(Long memberId, ReplyDTO replyForm) {
        Optional<Member> member= memberRepository.findById(memberId);
        Optional<Product> product = productRepository.findById(replyForm.getProductId());
        ProductReply productReply = new ProductReply();
        log.info(member.toString());
        log.info(product.toString());


        if(member.isPresent() && product.isPresent()) {
        	
            productReply.setReply(replyForm.getReply());
            productReply.setProduct(product.get());
            productReply.setMember(member.get());
            productReply.setUpdateDate(LocalDateTime.now());
            productReply.setCreateDate(LocalDateTime.now());
            replyRepository.save(productReply);
        }
        return productReply.getReplyId();
    }


    // 게시글에 해당하는 댓글 목록 조회
    @Override
    public List<ReplyDTO> getAllReplies(Long bid) {
        return replyRepository.findAll(bid)
                .stream().map(r -> new ReplyDTO(r))
                .collect(Collectors.toList());
    }
    // 댓글 한개 조회
    @Override
    public ReplyDTO getOneReply(Long rid) {
        return new ReplyDTO(replyRepository.findByRid(rid));
    }

    // 댓글 삭제
    @Transactional
    @Override
    public void deleteReply(Long rid) {
        replyRepository.remove(rid);
    }







}

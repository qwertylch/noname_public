package com.noname.dto;

import com.noname.entity.Member;
import com.noname.entity.Product;
import com.noname.entity.ProductReply;
import com.noname.util.DateConverter;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReplyDTO {

    // 제품 아이디
    private Long productId;
    // 댓글 아이디
    private Long replyId;
    // 댓글 작성자
    private String replyer;
    // 댓글 내용
    private String reply;

    private String createDate;


    // Entity -> DTO
    public ReplyDTO(ProductReply reply){
        this.productId = reply.getProduct().getProductId();
        this.replyId = reply.getReplyId();
        this.replyer = reply.getMember().getName();
        this.reply = reply.getReply();
        this.createDate = DateConverter.formatTimeAgo(reply.getCreateDate());
    }

    // DTO -> Entity
    public ProductReply toEntity() {
        ProductReply rep = new ProductReply();
        rep.setReplyId(this.replyId);
        rep.setReply(this.reply);

        // Product 엔티티 설정
        Product product = new Product();
        product.setProductId(this.productId);
        rep.setProduct(product);

        // Member 엔티티 설정
        Member member = new Member();
        member.setName(this.replyer);
        rep.setMember(member);

        return rep;
    }

}

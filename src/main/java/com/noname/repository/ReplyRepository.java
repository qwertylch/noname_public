package com.noname.repository;

import com.noname.entity.ProductReply;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReplyRepository {

    private final EntityManager em;

    // 저장
    public Long save(ProductReply reply){
        em.persist(reply);
        return reply.getProduct().getProductId();
    }
    // 댓글 rid로 조회
    public ProductReply findByRid(Long replyId) {
        return em.find(ProductReply.class, replyId);
    }
    // 본문에 해당하는 댓글 목록 조회
    public List<ProductReply> findAll(Long productId) {
        return em.createQuery(
                "select r from ProductReply r where r.product.productId = :productId order by r.createDate asc",
                        ProductReply.class)
                .setParameter("productId", productId)
                .getResultList();
    }
    // 삭제
    public void remove(Long replyId) {
        ProductReply findReply = em.find(ProductReply.class, replyId); // 영속상태
        em.remove(findReply); // entity 주고 삭제
    }
}

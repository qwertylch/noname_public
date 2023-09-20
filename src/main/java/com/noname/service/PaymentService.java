package com.noname.service;

import com.noname.dto.BiddingStatusDTO;
import com.noname.dto.PayResDTO;
import com.noname.entity.Bidding;
import com.noname.entity.Payment;

import java.util.List;

public interface PaymentService {
     List<BiddingStatusDTO> getAllMyBids(Long sid);

     Bidding findByBidId(Long bidId);

     void verifyRequest(String paymentKey, String orderId, Long amount);

     PayResDTO requestFinalPay(String paymentKey, String orderId, Long amount);

     void createPaymentFromPayResDTO(PayResDTO payResDTO, Long sid);

     List<Payment> findAllBySid(Long sid);

    void cancelPaymentFromOrderId(String payOrderId);
}

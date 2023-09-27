package com.noname.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.noname.dto.BiddingStatusDTO;
import com.noname.dto.PayResDTO;
import com.noname.entity.Bidding;
import com.noname.entity.Member;
import com.noname.entity.Payment;
import com.noname.enums.OrderStatus;
import com.noname.repository.BiddingRepository;
import com.noname.repository.MemberRepository;
import com.noname.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final BiddingRepository biddingRepository;
    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;


    public Member findMemberBySid(Long sid) {
        return memberRepository.findById(sid).get();
    }

    @Override
    public List<BiddingStatusDTO> getAllMyBids(Long sid) {

        return biddingRepository.findMaxBiddingWithAuctionStatusForEachProductByMemberSid(sid);

    }

    @Override
    public Bidding findByBidId(Long bidId) {

      return biddingRepository.findByBidId(bidId);
    }

    @Override
    public void verifyRequest(String paymentKey, String orderId, Long amount) {

    }

    @Override
    public PayResDTO requestFinalPay(String paymentKey, String orderId, Long amount) {
        String jsonBody = "{\"paymentKey\":\"" + paymentKey + "\",\"amount\":" + amount + ",\"orderId\":\"" + orderId + "\"}";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.tosspayments.com/v1/payments/confirm"))
                .header("Authorization", "Basic dGVzdF9za19rWUc1N0ViYTNHWmJkUkRBbXp6OHBXRE94bUExOg==")
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response.body());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(response.body(), PayResDTO.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON response", e);
        }

    }

    @Override
    public void createPaymentFromPayResDTO(PayResDTO payResDTO, Long sid) {
            Member foundMember = findMemberBySid(sid);
            Payment payment = new Payment();

            String payType = payResDTO.getPayType();
            Long amount = payResDTO.getAmount();
            String customerName = payResDTO.getCustomerName();
            String customerAddress = payResDTO.getCustomerAddress();
            String orderId = payResDTO.getOrderId();
            String orderName = payResDTO.getOrderName();

            payment.setMember(foundMember);
            payment.setPayType(payType);
            payment.setAmount(amount);
            payment.setCustomerName(customerName);
            payment.setCustomerAddress(customerAddress);
            payment.setOrderId(orderId);
            payment.setOrderName(orderName);
            payment.setOrderStatus(OrderStatus.PAID);

            paymentRepository.save(payment);

        }

    @Override
    public List<Payment> findAllBySid(Long sid) {
        return paymentRepository.findAllBySid(sid);
    }


    @Override
    @Transactional
    public void cancelPaymentFromOrderId(String payOrderId) {
        Payment deletePayment = paymentRepository.findByOrderId(payOrderId);
        log.info("======delete payment! : {}", deletePayment);
        paymentRepository.delete(deletePayment);
    }
}




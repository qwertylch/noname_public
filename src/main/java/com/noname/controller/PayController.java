package com.noname.controller;

import com.noname.dto.BiddingStatusDTO;
import com.noname.dto.PayResDTO;
import com.noname.entity.Address;
import com.noname.entity.Bidding;
import com.noname.entity.MemberImage;
import com.noname.entity.Payment;
import com.noname.entity.ProductImage;
import com.noname.security.CustomUser;
import com.noname.service.AddrService;
import com.noname.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
@Slf4j
public class PayController {
    final static Long MEMBER_ID = 1L;
    private final PaymentService paymentService;
    private final AddrService addrService;

    @GetMapping("/list")
    public String myList(@AuthenticationPrincipal CustomUser user, Model model) {
//        session.setAttribute("sid", 1L);
//        Long sid = (Long) session.getAttribute("sid");
    	
    	if(user == null) {
    		return "redirect:/login";
    	}
    	
    	Long sid = user.getMember().getMemberId();
        List<BiddingStatusDTO> bids = paymentService.getAllMyBids(sid);
        log.info("====bids : {}", sid);
        log.info("====bids : {}", bids);
        log.info("====bids : {}", bids.toString());
        model.addAttribute("result", bids);
        return "mypage/list";
    }

    @GetMapping("/paycart/{bidId}")
    public String payorderForm(@AuthenticationPrincipal CustomUser user,
                               @PathVariable Long bidId,
                               Model model) {
    	if(user == null) {
    		return "redirect:/login";
    	}
    	Long sid =user.getMember().getMemberId();
        Bidding bidResult = paymentService.findByBidId(bidId);
        Address activeAddress = addrService.findOneAddrBySid(sid);
        String orderId = UUID.randomUUID().toString();
        
        String image = convertImage(bidResult.getProduct().getProductImages().get(0));
     
        model.addAttribute("image", image);
        model.addAttribute("bidResult", bidResult);
        model.addAttribute("activeAddress", activeAddress);
        model.addAttribute("orderId", orderId);

        return "mypage/paycart";
    }

    @GetMapping("/success")
    public String successPay(@AuthenticationPrincipal CustomUser user,
                             @RequestParam String paymentKey,
                             @RequestParam String orderId,
                             @RequestParam Long amount, Model model) {
    	
        log.info("==== paymentKey: {}", paymentKey);
        log.info("==== orderId: {}", orderId);
        log.info("==== amount: {}", amount);

        PayResDTO payResDTO = paymentService.requestFinalPay(paymentKey, orderId, amount);
        Long sid =user.getMember().getMemberId();
        paymentService.createPaymentFromPayResDTO(payResDTO, sid);
        model.addAttribute("result", payResDTO);
        return "mypage/success";
    }

    @GetMapping("/successList")
    public String successList(@AuthenticationPrincipal CustomUser user, Model model) {
    	Long sid = user.getMember().getMemberId();
        List<Payment> result = paymentService.findAllBySid(sid);
        log.info("==== result: {}", result);
        model.addAttribute("result", result);
        return "mypage/successList";
    }


    
    @PostMapping("/cancel")
    public String cancelOrder(@RequestParam("payOrderId") String payOrderId) {
        log.info("====payOrderId : {}", payOrderId);
        paymentService.cancelPaymentFromOrderId(payOrderId);

        return "mypage/successList";
    }



    
    

    private String convertImage(ProductImage image) {
    	if(image != null) {
            return encodeImageURL(image.getPath() + File.separator + image.getIdentifier() + image.getName());
    	}
    	return null;

    }
    
    

    
    
    
    private String encodeImageURL(String url) {
        try {
            return URLEncoder.encode(url, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error encoding image URL");
        }
    }





}

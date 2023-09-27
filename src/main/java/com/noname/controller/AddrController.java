package com.noname.controller;

import com.noname.entity.Address;
import com.noname.security.CustomUser;
import com.noname.service.AddrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/addr")
@RequiredArgsConstructor
@Slf4j
public class AddrController {

    private final AddrService addrService;


    @GetMapping("/addrList")
    public String addrList(@AuthenticationPrincipal CustomUser user, Model model) {
//      session.setAttribute("sid", 1L);
//      Long sid = Long.parseLong(session.getAttribute("sid").toString());
//      log.info("====sid1 : {}", sid);
    	if(user == null ) {
    		return "redirect:/login";
    	}
    	Long sid = user.getMember().getMemberId();
        List<Address> addresses = addrService.findAddrListByMember(sid);
        model.addAttribute("addressInfo", addresses);
        return "addr/addrList";
    }

    @PostMapping("/addrList")
    public String addrPro(@AuthenticationPrincipal CustomUser user,
                          Address address,
                          Boolean addrCheckbox) {
    	
    	if(user == null ) {
    		return "redirect:/login";
    	}

    	Long sid = user.getMember().getMemberId();
        addrService.checkAndSaveAddr(sid, address, addrCheckbox);
        return "redirect:/addr/addrList";
    }

    @PostMapping("/delete")
    public String deleteAddr(Long addressId) {
        log.info("====addressId : {}", addressId);
        addrService.deleteAddrById(addressId);
        return "redirect:/addr/addrList";
    }

    @PostMapping("/update")
    public String updatePro(@AuthenticationPrincipal CustomUser user,
                            Address address,
                            Boolean addrCheckbox,
                            Long addressId) {
    	if(user == null ) {
    		return "redirect:/login";
    	}
    	Long sid = user.getMember().getMemberId();
        log.info("====addressId : {}", addressId);
        addrService.updateAddr(sid, address, addrCheckbox, addressId);
        return "redirect:/addr/addrList";
    }





}

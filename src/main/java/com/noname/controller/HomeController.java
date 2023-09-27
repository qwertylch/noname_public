package com.noname.controller;

import com.noname.entity.Member;
import com.noname.security.CustomUser;
import com.noname.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;

    @RequestMapping("/")
    public String home(@AuthenticationPrincipal CustomUser customUser, Model model) {

        log.info("home customUser : {}", customUser);
        if(customUser == null) { // 로그인 안했다
            return "home";
        }

        //Member member = customUser.getMember();
       
        model.addAttribute("member", customUser.getMember());
        return "home";
    }

    // 로그인 폼 (로그인 처리는 시큐리티가 알아서)
    @GetMapping("/login")
    public String loginForm(@ModelAttribute Member member, String prevPage) {
        return "login";
    }
    // 로그아웃도 시큐리티가 알아서 : post요청이면 됨

    // 회원 가입
    @GetMapping("/new")
    public String newForm(@ModelAttribute Member member) {
        return "newForm";
    }



    @PostMapping("/new")
    public String newPro(Member member, RedirectAttributes rttr) {
        log.info("member : {}", member);
        Member savedMember = memberService.save(member);
        log.info("savedMember : {}", savedMember);
        rttr.addAttribute("status", true); // 쿼리스트링으로 리다이렉트 뒤에 붙혀준다. ?status=true
        // html에서 status 꺼낼때 ${param.status}
        rttr.addAttribute("id", savedMember.getEmail()); // return하는 redirect 주소의 {id} 값 체워주기위해 rttr에 id 추가
        return "redirect:members/{id}"; // 회원 상세 페이지로 이동
    }

    /*
    @RequestMapping("/")
    public String home(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        // getSession(boolean create) : false : 세션이 없으면 안만들기 ->  null
        // true (default) : 세션이 없으면 새로 생성해라~
        if(session == null) { // 처음 방문
            return "home";
        }
        Member loginMember = (Member) session.getAttribute("loginMember");
        if(loginMember == null) { // 로그인 안했다
            return "home";
        }
        // 로그인 했다
        model.addAttribute("member", loginMember);
        return "loginHome";
    }*/


    /*
    @PostMapping("/login")
    public String loginPro(@ModelAttribute Member member, HttpSession session, Model model) {
        log.info("login member : {}", member);
        Member loginMember = memberService.login(member);
        if(loginMember != null) {
            session.setAttribute("loginMember", loginMember);
            return "redirect:/"; // 로그인 후 홈으로 이동
        }else {
            model.addAttribute("loginFail", true);
            return "member2/login"; // 폼 html 보여주기
        }
    }*/

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "/";
    }
}

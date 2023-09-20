package com.noname.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    // 컨트롤러 가기 전에 호출
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        log.info("로그인 체크 인터셉터 requestURI : {}", requestURI);

        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("loginMember") == null) {
            log.info("로그인 안했다....");
            response.sendRedirect("/login?prevPage=" + requestURI);
            return false; // 필터 체인 중지, 로그인폼으로 가라~
        }

        return true; // 다음 필터 또는 컨트롤러로 가라~~
    }
}

package com.noname.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class LogFilter implements Filter {

    // 필터 초기화 메서드 (생성자 같은 역할)
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init!!! ");
    }
    // HTTP 요청이 들어올대마다 자동으로 호출되는 메서드
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        log.info("doFilter requestURI = {}", requestURI);
        // return; // 다음으로 진행하지 않고 필터 끝내기

        chain.doFilter(request, response); // 중요! 다음 필터 체인으로 이동

    }

    // 필터 종료 메서드 (서블릿 컨테이너 종료시 호출)
    @Override
    public void destroy() {
        log.info("log filter destroy....");
    }
}

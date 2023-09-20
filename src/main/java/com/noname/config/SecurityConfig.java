package com.noname.config;

import com.noname.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.UUID;

@Configuration // 스프링 환경설정 파일임을 명시. 빈으로 자동 등록
@EnableWebSecurity // 모든 요청 URL은 스프링 시큐리티의 제어를 받도록 해주는 어노테이션
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService; // rememberMe 할때 필요, 주입받기
    private final DataSource dataSource; // DB 활용 rememberMe할때 필요

    //HttpSecurity (구: configure(HttpSecurity http))
    // 시큐리티 접근 제한, 로그인, 로그아웃 등 http관련 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and()
                .authorizeRequests()
                .antMatchers("/members/**").authenticated()
                //.antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .antMatchers("/mainimgs/**", "/css/**", "/new/**").permitAll()
                .anyRequest().permitAll()

                .and()
                .rememberMe()
                .userDetailsService(userDetailsService)
                .tokenRepository(tokenRepository())
                //.rememberMeParameter("rememberMe") // remember-me가 default값
                //.tokenValiditySeconds(3600)  // default 14일
                //.alwaysRemember(false) // 체크박스 활성화 하지 않아도 항상 remember-me 적용 default가 false
                //.key(UUID.randomUUID().toString())

                .and()
                .formLogin()  // 로그인 폼 설정
                .loginPage("/login") // 로그인폼 경로
                .usernameParameter("email") // 로그인 아이디가 넘어오는 파라미터명
                .passwordParameter("password")
                .defaultSuccessUrl("/") // 로그인 성공하면 이동할 경로
                //.successHandler(new CustonSuccessHanlder())
                .failureUrl("/login") // 로그인 실패시 이동할 경로

                .and()
                .logout() // 로그아웃 설정
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .deleteCookies("remember-me")
                .logoutSuccessUrl("/")
                .and()
                .csrf().disable(); // csrf 비활성화

        return http.build();
    }

    // WebSecurity : 시큐리티 적용 안할 경로 지정
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web
                .ignoring()
                .antMatchers("/css/**", "/mainimgs/**", "/new/**");
    }

    // AuthenticationManager : 시큐리트 인증 담당 : UserDetailsService 구현 클래스, PasswordEncoder 필요
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // BCryptPassword : 비밀번호 암호화 해주는 클래스 빈으로 등록 (시큐리티에서 비밀번호 암호화 강제함)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 데이터베이스 사용한 remember-me 적용시, DB직접 접속해서 insert, delete 처리 자동으로해줌
    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }




}

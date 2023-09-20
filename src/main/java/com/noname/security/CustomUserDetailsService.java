package com.noname.security;


import com.noname.entity.Member;
import com.noname.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

   // 시큐리티가 로그인처리해주고, 로그인시 자동으로 호출되는 메서드
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member findMember = memberRepository.findByEmail(username);
        if(findMember == null) {
            //throws new UsernameNotFoundException("해당 사용자가 존재하지 않습니다 : " + username);
        }
        return new CustomUser(findMember);
    }
}
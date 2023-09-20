package com.noname.security;

import com.noname.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Arrays;
import java.util.Collection;

@Getter
public class CustomUser extends User {

    private Member member; // 우리가 사용하는 회원정보 담는 객체, 내부에 추가

    public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }
    // 우리 버전의 생성자 --> 부모 생성자 호출하는 패턴은 유지
    public CustomUser(Member member){
        super(member.getEmail(), member.getPassword(),
                Arrays.asList(new SimpleGrantedAuthority(member.getRole().getValue())));
        this.member = member;
    }

}

package com.khumu.alimi.service;

import com.khumu.alimi.data.dto.SimpleKhumuUserDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


// 우리는 딱히 실존하는 유저인지는 찾을 필요 없고, 그냥 token이 verify만 됐으면 됨.
// UserDetailsService type이 Auth manager에 의해 자동으로 수행된다.
@Slf4j
@Service
@Getter
@RequiredArgsConstructor
public class AuthUserDetailsServiceImpl implements UserDetailsService {
     // 해당 id의 유저가 존재하는가.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // spring security 때문에 가짜로 구현.
        // 원래는 login 할 때 이용하는 거.
        return null;
    }
}

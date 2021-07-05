package com.example.demo.auth;

import com.example.demo.Exception.NotFoundException;
import com.example.demo.Obj.app_user.AppUser;
import com.example.demo.Service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

//驗證方法
/*
docs.spring.io
UserDetailsService

locates the user based on username.
 */

@Service
public class SpringUserService implements UserDetailsService {

    @Autowired
    private AppUserService appUserService;

//    user參數 傳入登入頁面輸入的帳號
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
//            從DB 用username 找出user後 用帳號與密碼 建立User物件 給驗證機制使用
            AppUser appUser = appUserService.getUserByEmail(username);

            List<SimpleGrantedAuthority> authorities = appUser.getAuthorities().stream()
                    .map(auth -> new SimpleGrantedAuthority(auth.name()))
                    .collect(Collectors.toList());

            return new SpringUser(appUser);

        } catch (NotFoundException e) {
            throw new UsernameNotFoundException("Username is wrong.");
        }
    }
}

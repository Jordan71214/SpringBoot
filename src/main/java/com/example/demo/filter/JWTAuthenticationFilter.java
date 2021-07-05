package com.example.demo.filter;

import com.example.demo.Service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


@Component
public class    JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    protected JWTService jwtService;

    @Autowired
    protected UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response
            , FilterChain chain) throws ServletException, IOException {
//        從request header取出 放在AUTHORIZATION欄位的access token -> "AUTHORIZATION":"Bearer AccessToken"
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null) {
//            取出Access Token
            String accessToken = authHeader.replace("Bearer ", "");

//            解析access token
            Map<String, Object> claims = jwtService.parseToken(accessToken);

//            取出payload的username
            String username = (String) claims.get("username");
//            驗證user是否有效
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);


            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//            put the authentication into the SecurityContext
//            change the currently authenticated principle.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);

    }
}

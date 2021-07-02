package com.example.demo.Service;

import com.example.demo.objRequest.AuthRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Calendar;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JWTService {

//  帳密驗證
    @Autowired
    private AuthenticationManager authenticationManager;


    private final String KEY = "KenIsRunningBlogForProgrammingBeginner.";

//  產生JWT
    public String generateToken(AuthRequest request) {

//      帳密驗證
//      AuthenticationManager 接收Authentication介面物件, 可接收多種驗證方式, 以帳密驗證, 使用UsernamePasswordAuthenticationToken

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
//      AuthenticationManager 接收到Authentication後, 會使用SecurityConfig的userDetailsService與passwordEncoder協助驗證帳號與密碼
//      驗證, 得到UsernamePasswordAuthenticationToken物件, 使用Authentication接收
        authentication = authenticationManager.authenticate(authentication);

//      principle 會變成UserDetailsService的return -> UserDetails物件
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

//      JWT
//      JWT 包含 header, payload, signature

//      實作payload
//      payload就是存放自己的資料，對外聲明的資料(claims)
//      RFC 7519提供 payload建議存放的資料，可使用函式庫內建的方法存放
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 2);

        Claims claims = Jwts.claims();
        claims.put("username", userDetails.getUsername());
        claims.setExpiration(calendar.getTime());
        claims.setIssuer("Programming Classroom");

//      實作signature -> 把header, payload 用密鑰 secretKey 加密編碼
//      產生密鑰
        Key secretKey = Keys.hmacShaKeyFor(KEY.getBytes());

//      實作JWT
//      builder() 內建header
//      setClaims() 設置payload
//      signWith() 用密鑰加密,簽名
//      compact() 產生Token:String
        return Jwts.builder()
                .setClaims(claims)
                .signWith(secretKey)
                .compact();
    }


//    解析資料
    public Map<String, Object> parseToken(String token) {
        Key secretKey = Keys.hmacShaKeyFor(KEY.getBytes());


//      解析器, 把密鑰傳入
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build();


//      取得payload (Claims) -> 使用解析器, 傳入token:String, get the original JWS
//      getBody()方法 取得JWS的payload(Claims)
        Claims claims = parser
                .parseClaimsJws(token)
                .getBody();

        return claims.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}

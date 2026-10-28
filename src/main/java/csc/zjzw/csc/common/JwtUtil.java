package csc.zjzw.csc.common;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey key;
    private final long expireMillis;
    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expire-minutes:120}") long expireMinutes) {
        // 用密钥字符串生成 HMAC 密钥对象
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expireMillis = expireMinutes * 60 * 1000;
    }
    /** 签发 Token */
    public String createToken(Integer userId, String userName) {
        Date now = new Date();
        return Jwts.builder()
                .subject(String.valueOf(userId))                      // 标准字段：主体（放用户 id）
                .claim("name", userName)                              // 自定义字段
                .issuedAt(now)                                        // 签发时间
                .expiration(new Date(now.getTime() + expireMillis))   // 过期时间
                .signWith(key)                                        // 用密钥签名
                .compact();                                           // 生成字符串
    }

    /** 解析并验签（失败会抛 JwtException：过期、被篡改、格式错误） */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

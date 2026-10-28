package csc.zjzw.csc.interceptor;

import csc.zjzw.csc.common.BusinessException;
import csc.zjzw.csc.common.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);

    private final JwtUtil jwtUtil;
    public LoginInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 把开始时间存到 request 里，afterCompletion 时取出来
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            throw new BusinessException(401, "未登录，请先登录");
        }
        String token = auth.substring(7).trim();
        // ② 验签 + 解析（失败会抛 JwtException）
        Claims claims;
        try {
            claims = jwtUtil.parseToken(token);
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Token 校验失败：{}", e.getMessage());
            throw new BusinessException(401, "Token 无效或已过期");
        }

        // ③ 把用户信息塞进 request，供 Controller 使用
        request.setAttribute("userId", Integer.valueOf(claims.getSubject()));
        request.setAttribute("userName", claims.get("name"));
        return true;
    }


}

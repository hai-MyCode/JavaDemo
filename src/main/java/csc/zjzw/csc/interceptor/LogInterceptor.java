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
public class LogInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(LogInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute("startTime", System.currentTimeMillis());
        log.info("→ 请求开始：{} {}", request.getMethod(), request.getRequestURI());
        return true;      // 放行
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        Long start = (Long) request.getAttribute("startTime");
        long cost = (start == null) ? -1 : System.currentTimeMillis() - start;
        log.info("← 请求结束：{} {}，耗时 {} ms",
                request.getMethod(), request.getRequestURI(), cost);
    }
}

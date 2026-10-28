package csc.zjzw.csc.common;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    /** 加密（注册、改密码时用） */
    public String encode(String password) {
        return encoder.encode(password);
    }
    /** 验证（用户输入的密码和数据库中的密码是否匹配） */
    public boolean matches(String password, String encodedPassword) {
        return encoder.matches(password, encodedPassword);
    }
}

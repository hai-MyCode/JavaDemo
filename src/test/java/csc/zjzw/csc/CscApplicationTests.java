package csc.zjzw.csc;

import csc.zjzw.csc.common.BusinessException;
import csc.zjzw.csc.common.PasswordUtil;
import csc.zjzw.csc.entity.User;
import csc.zjzw.csc.mapper.UserMapper;
import csc.zjzw.csc.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;        // ← 关键：加上

@SpringBootTest
class CscApplicationTests {

    @Autowired private UserMapper userMapper;
    @Autowired private PasswordUtil passwordUtil;

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
    }

    /** BCrypt 核心行为：同一密码两次加密结果不同（有盐）；matches 能正确区分对错 */
    @Test
    void bcrypt_同一个密码两次加密结果应该不同() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String h1 = encoder.encode("123456");
        String h2 = encoder.encode("123456");

        assertNotEquals(h1, h2, "有随机盐，两次加密结果必须不同");
        assertTrue(encoder.matches("123456", h1), "正确密码应该校验通过");
        assertTrue(encoder.matches("123456", h2));
        assertFalse(encoder.matches("000000", h1), "错误密码应该校验失败");
    }

    /** 正确密码 → 应该拿到 Token */
    @Test
    @Transactional
    void login_正确密码应该成功() {
        // ① 自己造一个用户（事务结束后自动回滚）
        String name = "测试用户_" + System.nanoTime();      // 用随机名，避免撞车
        User u = new User();
        u.setUsername(name);
        u.setAge(20);
        u.setPassword(passwordUtil.encode("123456"));
        userMapper.insert(u);

        // ② 用刚造的用户登录
        String token = userService.login(name, "123456");

        // ③ 断言
        assertNotNull(token);
    }

    /** 错误密码 → 应该抛 401 */
    @Test
    void login_错误密码应该失败() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> userService.login("陈致远", "错误的密码"));

        assertEquals(401, ex.getCode(), "错误密码应该返回 401");
    }

    /** 修改用户 → 应该有断言验证结果 */
    @Test
    @Transactional                                        // 跑完自动回滚，不污染数据
    void update_修改年龄后应该能查到新值() {
        User user = new User();
        user.setId(11);                                   // ← 用一个【确实存在】的 id
        user.setAge(30);
        userService.update(user);

        User updated = userService.getById(11);
        assertEquals(30, updated.getAge(), "年龄应该被改成 30");
    }
}
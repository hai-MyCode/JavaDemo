package csc.zjzw.csc.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import csc.zjzw.csc.common.BusinessException;
import csc.zjzw.csc.common.JwtUtil;
import csc.zjzw.csc.common.PasswordUtil;
import csc.zjzw.csc.entity.User;
import csc.zjzw.csc.mapper.UserMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserMapper userMapper;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final JwtUtil jwtUtil;
    private final PasswordUtil passwordUtil;
    public UserService(UserMapper userMapper, JwtUtil jwtUtil, PasswordUtil passwordUtil) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.passwordUtil = passwordUtil;
    }
    public List<User> list() {
        return userMapper.selectList(null);
    }

    //登录接口  登录：校验用户名密码，成功则返回 Token */
    public String login(String userName, String password) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, userName).last("LIMIT 1"));        // ← 加这一行：有多条时只取第一条);

        if(userName == null || password==null) {
            throw new BusinessException(401, "用户名或者密码必须填写");
        }
        // ⚠️ 注意：用户名错误和密码错误返回【同一句话】，防止被人枚举账号
        if (user == null || !passwordUtil.matches(password, user.getPassword())) {
            log.warn("登录失败：用户名 = {}", userName);        // ← 加这一行
            throw new BusinessException(401, "用户名或密码错误");
        }
        log.info("用户登录成功：id = {}，姓名 = {}", user.getId(), user.getUsername());
        return jwtUtil.createToken(user.getId(), user.getUsername());
    }

    //根据id获取用户
    public User getById(Integer id) {
        User user = userMapper.selectById(id);
        if (user == null){
            throw new BusinessException(400,"未找到该用户id:" + id);
        }
        return user;
    }

    //添加用户
    public void add(User user) {
        if (user.getUsername() == null || user.getUsername().isBlank()) throw new BusinessException(400,"参数错误");
        int row =  userMapper.insert(user);
        if (row == 0) throw new BusinessException(400,"添加失败");
        log.info("新增用户成功，id = {}，姓名 = {}", user.getId(), user.getUsername());
    }

    //更新数据
    public void update(User user) {
        if(user.getId() == null){
            throw new BusinessException(400,"参数错误");
        }
        int row = userMapper.updateById(user);
        if(row == 0){
            throw new BusinessException(400,"该用户不存在");
        }
        log.info("更新用户成功，id = {}，姓名 = {}", user.getId(), user.getUsername());
    }
    //删除用户
    public void delete(Integer id){
        if (id == null) throw new BusinessException(400,"参数错误");
        int row = userMapper.deleteById(id);
        if (row == 0) throw new BusinessException(404,"该用户不存在");
        log.info("删除用户成功，id = {}", id);
    }

    //分页获取用户
    public IPage<User> page(Integer pageNum , Integer pageSize){
        Page<User> page = new Page<>(pageNum, pageSize);
        page.addOrder(OrderItem.asc("id"));
        return userMapper.selectPage(page, null);
    }

    //批量添加用户
    @Transactional(rollbackFor = Exception.class)
    public void batchAdd(List<User> users) {
        for (User user : users){
            userMapper.insert(user);
        }
    }
}

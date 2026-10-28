package csc.zjzw.csc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import csc.zjzw.csc.common.JwtUtil;
import csc.zjzw.csc.common.Result;
import csc.zjzw.csc.common.validation.AddGroup;
import csc.zjzw.csc.common.validation.LoginGroup;
import csc.zjzw.csc.common.validation.UpdateGroup;
import csc.zjzw.csc.entity.User;
import csc.zjzw.csc.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
//@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        System.out.println("🔥 UserController 被创建了，hashCode = " + this.hashCode());
    }

    //获取当前用户信息
    @GetMapping("/my")
    public Result<String> my(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        String userName = (String) request.getAttribute("userName");
        return Result.success("当前登录用户：id = " + userId + "，姓名 = " + userName);
    }

    //登录接口
    @PostMapping("/login")
    public Result<String> login(@Validated(LoginGroup.class) @RequestBody User user) {
        return Result.success(userService.login(user.getUsername(), user.getPassword()));
    }
    @GetMapping
    public Result<List<User>> list() {
        return Result.success(userService.list());
    }
    //获取分页的数据
    @GetMapping("/page")
    public Result<IPage<User>> page(@RequestParam(defaultValue = "1")
                                    @Min(value = 1 , message = "页码不能小于1！") Integer pageNum,
                              @RequestParam(defaultValue = "10")
                              @Min(value = 1 ,message = "每页至少 1 条！")
                              @Max(value = 100 ,message = "每页最多 100 条！") Integer pageSize){
        return Result.success(userService.page(pageNum, pageSize));
    }
    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
    }
    @PostMapping
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody User user) {
        userService.add(user);
        return Result.success();
    }


    @PutMapping
    public Result<Void> update(@Validated(UpdateGroup.class) @RequestBody User user) {
        userService.update(user);
        return Result.success();
    }
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        userService.delete(id);
        return Result.success();
    }
    //批量添加用户
    @PostMapping("/batch")
    public Result<Void> batchAdd(@RequestBody List<User> users) {
        userService.batchAdd(users);
        return Result.success();
    }
}

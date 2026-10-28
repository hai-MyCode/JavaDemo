package csc.zjzw.csc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import csc.zjzw.csc.common.validation.AddGroup;
import csc.zjzw.csc.common.validation.LoginGroup;
import csc.zjzw.csc.common.validation.UpdateGroup;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@TableName("sys_user")
public class User {

    @NotNull(groups = UpdateGroup.class , message = "ID不能为空")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @NotNull(groups = {AddGroup.class, UpdateGroup.class}, message = "年龄不能为空")
    @Min(value = 0,   groups = {AddGroup.class, UpdateGroup.class}, message = "年龄不能小于 0")
    @Max(value = 127, groups = {AddGroup.class, UpdateGroup.class}, message = "年龄不能超过 127")
    private Integer age;
    private Integer score;
    @NotBlank(groups = {AddGroup.class, LoginGroup.class}, message = "账号不能为空")
    private String username;
    @NotBlank(groups = {LoginGroup.class}, message = "密码不能为空")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank(groups = {UpdateGroup.class}, message = "昵称不能为空")
    private String nickname;

    private Integer status;
    private LocalDateTime createTime;   // import java.time.LocalDateTime; ← 别用 java.util.Date
    private LocalDateTime updateTime;



    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

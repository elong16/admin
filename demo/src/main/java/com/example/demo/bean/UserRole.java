package com.example.demo.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "user_role")
public class UserRole {
    private Integer uid;
    private Integer rid;
}

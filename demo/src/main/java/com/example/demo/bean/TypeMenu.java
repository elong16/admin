package com.example.demo.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "type_menu")
public class TypeMenu {
    private Integer tid;
    private Integer mid;
}

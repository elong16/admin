package com.example.demo.bean;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "type")
public class Type {
    private Integer id;
    private String name;
    @TableField("name_zh")
    private String name_Zh;

    public Type(Integer id, String name, String name_Zh) {
        this.id = id;
        this.name = name;
        this.name_Zh = name_Zh;
    }
}

package com.example.demo.bean;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@TableName(value = "menu")
public class Menu {
    private Integer id;
    private String name;
    private String path;
    private String comment;
    private String icon;
    private String perms;
    private Integer parentid;
    //type=0 目录 1 菜单 2 按钮

    private Integer type;
    @TableField(exist = false)
    private List<Type> roletypes;
    @TableField(exist = false)
    private List<Menu> children = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(path, menu.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }


}

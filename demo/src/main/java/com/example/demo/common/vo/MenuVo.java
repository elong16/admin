package com.example.demo.common.vo;

import com.example.demo.bean.Menu;
import lombok.Data;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class MenuVo implements Serializable {
    private Integer id;
    private String name;
    private String title;
    private String icon;
    private String path;
    private String component;
    private String perms;
    private List<MenuVo> children = new ArrayList<>();
}

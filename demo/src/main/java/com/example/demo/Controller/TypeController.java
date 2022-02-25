package com.example.demo.Controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.bean.Role;
import com.example.demo.bean.Type;
import com.example.demo.common.Result;
import com.example.demo.mapper.TypeMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/type")
public class TypeController {
    @Resource
    TypeMapper typeMapper;

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){

        LambdaQueryWrapper<Type> wrapper = Wrappers.<Type>lambdaQuery();
        if (StrUtil.isNotBlank(search))
        {
            wrapper.like(Type::getName,search);
        }
        Page<Type> typePage=typeMapper.selectPage(new Page<>(pageNum,pageSize), wrapper);
        return Result.success(typePage);
    }
}

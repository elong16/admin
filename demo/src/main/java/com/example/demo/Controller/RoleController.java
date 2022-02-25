package com.example.demo.Controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.bean.*;
import com.example.demo.common.Result;
import com.example.demo.mapper.RoleMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {


    //添加角色
    @PostMapping
    public Result<?> save(@RequestBody Role role) {
        roleMapper.insert(role);
        Role role1 = roleMapper.selectOne(Wrappers.<Role>lambdaQuery().eq(Role::getName, role.getName()));
        System.out.println("role1" + role1);
        RoleType roleType = new RoleType();
        Type type = typeMapper.selectOne(Wrappers.<Type>lambdaQuery().eq(Type::getName_Zh, role.getType()));
        System.out.println("tid" + type.getId());
        roleType.setTid(type.getId());
        roleType.setRid(role1.getId());
        roleTypeMapper.insert(roleType);
        System.out.println(roleType);
        return Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {

        LambdaQueryWrapper<Role> wrapper = Wrappers.<Role>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(Role::getName, search);
        }
        Page<Role> userPage = roleMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(userPage);
    }

    //修改
    @PutMapping
    public Result<?> update(@RequestBody Role role) {
        roleMapper.updateById(role);
        return Result.success();
    }


    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable long id) {
        roleMapper.deleteById(id);

        return Result.success();
    }

    @PostMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestBody List<Integer> ids) {
        roleMapper.deleteBatchIds(ids);
        return Result.success();
    }
}

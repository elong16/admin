package com.example.demo.Controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.bean.*;
import com.example.demo.common.Result;
import com.example.demo.mapper.*;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    @PreAuthorize("hasAuthority('user:add')")
    @PostMapping
    public Result<?> save(@RequestBody User user) {
        User isexist = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getNumber, user.getNumber()));
        if (isexist != null) {
            return Result.error("-1", "该用户已存在");
        } else {
            if (user.getMobile() == "true") {
                user.setMobile("是");
            } else {
                user.setMobile("否");
            }
            if (user.getSupper() == "true") {
                user.setSupper("是");
            } else {
                user.setSupper("否");
            }
            userMapper.insert(user);
            User userInData = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getNumber, user.getNumber()));
            Role role = roleMapper.selectOne(Wrappers.<Role>lambdaQuery().eq(Role::getName, user.getRole()));
            UserRole userRole = new UserRole();
            userRole.setUid(userInData.getId());
            userRole.setRid(role.getId());
            userRoleMapper.insert(userRole);

            return Result.success();
        }

    }

//    @PreAuthorize("hasAuthority('user:add')")
    @PutMapping
    public Result<?> update(@RequestBody User user) {
        userMapper.updateById(user);
        return Result.success();
    }



//    @PreAuthorize("hasAuthority('user')")
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {

        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(User::getUsername, search);
        }
        Page<User> userPage = userMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(userPage);
    }
//    @PreAuthorize("hasAuthority('user')")
    @GetMapping("/{id}")
    public Result<?> getUserById(@PathVariable long id) {
        User res = userMapper.selectById(id);
        return Result.success(res);
    }
//    @PreAuthorize("hasAuthority('user:delete')")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable long id) {
        userMapper.deleteById(id);
        return Result.success();
    }

//    @PostMapping("/login")
//    public Result<?> login(@RequestBody User user) {
//        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getNumber, user.getNumber()).eq(User::getPassword, user.getPassword()));
//        if (res == null) {
//            return Result.error("-1", "用户名或密码不正确");
//        }
//        HashSet<Menu> menuSet = new HashSet<>();
//        //1.从user_role表通过用户id查询用户的角色信息
//        Integer userId = res.getId();
//        user.setId(userId);
//        List<UserRole> userRoles = roleMapper.getUserRoleByUserId(userId);
//        for (UserRole userRole : userRoles) {
//            //2.根据RoleId查询角色类型
//            Integer roleId = userRole.getRid();
//            RoleType roleType = typeMapper.getRoleTypeByRoleId(roleId);
//            Integer typeid = roleType.getTid();
//            //3.根据角色类型查询对应功能
//            List<TypeMenu> typeMenus = menuMapper.getTypeMenuByTypeId(typeid);
//            for (TypeMenu typeMenu : typeMenus) {
//                Integer menuid = typeMenu.getMid();
//                Menu menu = menuMapper.selectById(menuid);
//                menuSet.add(menu);
//
//            }
//        }
//        LinkedHashSet<Menu> sortedSet = menuSet.stream().sorted(Comparator.comparing(Menu::getId)).collect(Collectors.toCollection(LinkedHashSet::new));
//        //4.设置当前的权限信息
//        res.setMenus(sortedSet);
//
//        return Result.success(res);
//    }
//    @PreAuthorize("hasAuthority(' user:delete')")
    @PostMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestBody List<Integer> ids) {
        userMapper.deleteBatchIds(ids);
        return Result.success();
    }
}

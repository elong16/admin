package com.example.demo.Controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.bean.Menu;
import com.example.demo.bean.Type;
import com.example.demo.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
//import com.example.demo.utils.RedisUtil;

public class BaseController {
    @Autowired
    HttpServletRequest request;
    @Autowired
    MenuMapper menuMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    TypeMapper typeMapper;

    @Autowired
    UserRoleMapper userRoleMapper;
    @Autowired
    RoleTypeMapper roleTypeMapper;
    @Autowired
    TypeMenuMapper typeMenuMapper;



    public String getUserAuthorityInfo(Integer userId) {
        String authority = "";
        List<Integer> roleids = roleMapper.getRoleIdByUserId(userId);
        List<Integer> typeids = new ArrayList<Integer>();
        HashSet<Integer> menuIds = new HashSet<Integer>();
        int i=0;
        System.out.println(userId);
        System.out.println("roleids"+roleids);
        for (Integer roleid:roleids) {
            Integer typeid = typeMapper.getTypeIdByRoleId(roleid);
            System.out.println(typeid);
            Type type=typeMapper.selectOne(Wrappers.<Type>lambdaQuery().eq(Type::getId,typeid));
            typeids.add(typeid);
            authority=authority.concat(type.getName());
            authority = authority.concat(",");
        }
        System.out.println(authority);
        System.out.println(typeids);
        for (Integer typeid : typeids ) {
            List<Integer> nowMenuIds = menuMapper.getMenuIdByTypeId(typeid);
            menuIds.addAll(nowMenuIds);
        }
        if (menuIds.size() > 0) {
            List<Menu> menus = new ArrayList<Menu>();
            for (Integer menuid : menuIds) {
                menus.add(menuMapper.getByMenuId(menuid));
            }
            String menuPerms=menus.stream().map(m->m.getPerms()).collect(Collectors.joining(","));
            authority=authority.concat(menuPerms);
        }

        System.out.println(authority);
        return authority;
    }
//    @Autowired
//    RedisUtil redisUtil;
}

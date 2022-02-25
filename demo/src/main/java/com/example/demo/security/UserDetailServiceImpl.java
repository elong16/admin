package com.example.demo.security;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.bean.*;
import com.example.demo.mapper.MenuMapper;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.mapper.TypeMapper;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    TypeMapper typeMapper;
    @Autowired
    MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //User user = userMapper.loadUserByUsername(username);
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getNumber, username));

        if (user == null) {
            throw new UsernameNotFoundException("用户名不正确");
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println("this is 权限"+getUserAuthority(user.getId()));
        return new AccountUser(user.getId(), user.getNumber(), bCryptPasswordEncoder.encode(user.getPassword()), true, true, true, true, getUserAuthority(user.getId()));
    }

    //获取菜单权限
    public List<GrantedAuthority> getUserAuthority(Integer userId) {
        String authority = getUserAuthorityInfo(userId);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }


    public String getUserAuthorityInfo(Integer userId) {
        String authority = "";
        List<Integer> roleids = roleMapper.getRoleIdByUserId(userId);
        List<Integer> typeids = new ArrayList<Integer>();
        HashSet<Integer> menuIds = new HashSet<Integer>();
        for (Integer roleid : roleids) {
            Integer typeid = typeMapper.getTypeIdByRoleId(roleid);
            Type type = typeMapper.selectOne(Wrappers.<Type>lambdaQuery().eq(Type::getId, typeid));
            typeids.add(typeid);
            authority = authority.concat(type.getName());
            authority = authority.concat(",");
        }
        for (Integer typeid : typeids) {
            List<Integer> nowMenuIds = menuMapper.getMenuIdByTypeId(typeid);
            menuIds.addAll(nowMenuIds);
        }
        if (menuIds.size() > 0) {
            List<Menu> menus = new ArrayList<Menu>();
            for (Integer menuid : menuIds) {
                menus.add(menuMapper.getByMenuId(menuid));
            }
            String menuPerms = menus.stream().map(m -> m.getPerms()).collect(Collectors.joining(","));
            authority = authority.concat(menuPerms);
        }

        return authority;
    }


}

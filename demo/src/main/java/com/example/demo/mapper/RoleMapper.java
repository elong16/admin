package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.bean.Role;
import com.example.demo.bean.UserRole;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface RoleMapper extends BaseMapper<Role> {

    @Select("select id from role where name=\"#{rolename}\"")
    Integer getRoleIdByRoleName(String rolename);

    @Select("select * from user_role where uid= #{userId}")
    List<UserRole> getUserRoleByUserId(Integer userId);

    @Select("select * from role where id=#{roleId}")
    Role getRoleById(Integer roleId);

    @Select("select rid from user_role where uid=#{userId}")
    List<Integer> getRoleIdByUserId(Integer userId);
}

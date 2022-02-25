package com.example.demo.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.bean.Role;
import com.example.demo.bean.RoleType;
import com.example.demo.bean.Type;
import com.example.demo.bean.UserRole;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TypeMapper extends BaseMapper<Type> {


    @Select("select * from role_type where rid= #{roleId}")
    RoleType getRoleTypeByRoleId(Integer roleId);

    //    @Select("select * from type where id=#{typeId}")
//    Type getTypeById(Integer typeId);
    @Select("select tid from role_type where rid= #{roleId}")
    Integer getTypeIdByRoleId(Integer roleid);

    @Select("select id from type where name=#\'#{typename}\'")
    Integer getTypeIdByTypeName(String typename);
}

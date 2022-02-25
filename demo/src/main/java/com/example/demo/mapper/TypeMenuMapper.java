package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.bean.TypeMenu;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TypeMenuMapper extends BaseMapper<TypeMenu> {
    @Select("select mid from type_menu where tid=#{typeid}")
    List<Integer> getMenuIdsByTypeId(Integer typeid);
}

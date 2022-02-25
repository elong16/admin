package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.bean.Menu;
import com.example.demo.bean.TypeMenu;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu> {
    @Select("select * from type_menu where tid=#{typeId}")
    List<TypeMenu> getTypeMenuByTypeId(Integer typeId);

    @Select("select mid from type_menu where tid=#{typeId}")
    List<Integer> getMenuIdByTypeId(Integer typeId);

    @Select("select * from menu where id=#{menuid}")
    Menu getByMenuId(Integer menuId);

    @Select("select * from menu")
    List<Menu> getAllMenu();


}

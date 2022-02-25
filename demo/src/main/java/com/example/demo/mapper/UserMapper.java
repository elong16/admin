package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.bean.User;
import org.apache.ibatis.annotations.Select;

public interface UserMapper extends BaseMapper<User> {
    @Select("select * from user where number=\'#{username}\'")
    User loadUserByUsername(String username);

    @Select("select * from user where number=\'#{account}\'")
    User getUserByUserNumber(String account);


}

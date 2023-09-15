package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from user where openid = #{openId}")
    User getByOpenId(String openId);

//    @Insert("insert into user (openid, create_time) values (#{openid}, #{createTime})")
    void insert(User user);

    @Select("select * from user where id = #{userId}")
    User getById(Long userId);
}

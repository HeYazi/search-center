package com.hyz.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hyz.springbootinit.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author hegd
 * @description 针对表【user(用户)】的数据库操作Mapper
 * @createDate 2023-06-13 22:53:56
 * @Entity com.hyz.springbootinit.model.entity.User
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}





package com.kob.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tong Lu
 * @description:
 * @create 2024-02-26 20:46
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}

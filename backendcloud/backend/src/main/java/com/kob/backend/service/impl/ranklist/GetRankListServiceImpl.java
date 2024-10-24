package com.kob.backend.service.impl.ranklist;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.ranklist.GetRankListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Tong Lu
 * @description:
 * @create 2024-03-11 19:45
 */
@Service
public class GetRankListServiceImpl implements GetRankListService {
    @Autowired
    private UserMapper userMapper;


    @Override
    public JSONObject getList(Integer page) {
        IPage<User> userIPage = new Page<>(page,5);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("rating");
        List<User> users = userMapper.selectPage(userIPage,queryWrapper).getRecords();

        for(User user :users){//清空密码，防止传向前端
            user.setPassword("");
        }

        JSONObject resp = new JSONObject();
        resp.put("users",users);
        resp.put("users_count",userMapper.selectCount(null));

        return resp;
    }
}

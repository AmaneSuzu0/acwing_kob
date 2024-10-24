package com.kob.botrunningsystem.service.impl;

import com.kob.botrunningsystem.service.BotRunningService;
import com.kob.botrunningsystem.service.impl.utils.Bot;
import com.kob.botrunningsystem.service.impl.utils.BotPool;
import org.springframework.stereotype.Service;

/**
 * @author Tong Lu
 * @description:
 * @create 2024-03-08 14:47
 */
@Service
public class BotRunningServiceImpl implements BotRunningService {
    public final static BotPool botpool = new BotPool();

    @Override
    public String addBot(Integer userId, String botCode, String gameInfo) {
        System.out.println("add bot" + userId);
        botpool.addBot(userId,botCode,gameInfo);
        return "add bot into botrunning success";
    }
}

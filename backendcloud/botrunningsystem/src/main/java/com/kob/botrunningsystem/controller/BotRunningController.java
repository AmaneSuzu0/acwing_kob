package com.kob.botrunningsystem.controller;

import com.kob.botrunningsystem.service.BotRunningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author Tong Lu
 * @description:
 * @create 2024-03-08 14:49
 */
@RestController
public class BotRunningController {

    @Autowired
    private BotRunningService botRunningService;

    @PostMapping("/bot/add/")
    public String addBot(@RequestParam MultiValueMap<String,String> data){
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        String botCode = data.getFirst("bot_code");
        String gameInfo = data.getFirst("game_info");
        return botRunningService.addBot(userId,botCode,gameInfo);
    }
}

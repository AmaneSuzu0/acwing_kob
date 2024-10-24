package com.kob.backend.controller.user.bot;

import com.kob.backend.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Tong Lu
 * @description:
 * @create 2024-02-29 17:53
 */
@RestController
public class AddController {
    @Autowired
    private AddService addService;
    @PostMapping("/api/user/bot/add/")
    public Map<String,String> add(@RequestParam Map<String,String> data){
        return addService.add(data);
    }
}

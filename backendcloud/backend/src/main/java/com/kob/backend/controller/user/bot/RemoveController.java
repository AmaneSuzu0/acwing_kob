package com.kob.backend.controller.user.bot;

import com.kob.backend.service.user.bot.RemoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Tong Lu
 * @description:
 * @create 2024-02-29 18:04
 */
@RestController
public class RemoveController {
    @Autowired
    private RemoveService removeService;
    @PostMapping("/api/user/bot/remove/")
    public Map<String,String> remove(@RequestParam Map<String,String> data){
        return removeService.remove(data);
    }
}

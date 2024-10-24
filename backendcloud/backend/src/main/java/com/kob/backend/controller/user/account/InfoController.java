package com.kob.backend.controller.user.account;

import com.kob.backend.service.user.account.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Tong Lu
 * @description:
 * @create 2024-02-27 17:51
 */
@RestController
public class InfoController {
    @Autowired
    private InfoService infoService;

    @GetMapping("/api/user/account/info/")
    public Map<String,String> getinfo(){
        return infoService.getinfo();
    }
}

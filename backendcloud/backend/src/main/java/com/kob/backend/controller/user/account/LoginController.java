package com.kob.backend.controller.user.account;

import com.kob.backend.service.user.account.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Tong Lu
 * @description:
 * @create 2024-02-27 17:03
 */
@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/api/user/account/token/")
    public Map<String,String> getToken(@RequestParam Map<String,String> map) throws Exception{
        String username = map.get("username");
        String password = map.get("password");
        System.out.println(username + " " + password);
        return loginService.getToken(username,password);
    }

}

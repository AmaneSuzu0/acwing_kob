package com.kob.backend.service.impl.user.account;

import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.LoginService;
import com.kob.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.net.Authenticator;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tong Lu
 * @description:
 * @create 2024-02-27 16:52
 */

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Map<String, String> getToken(String username, String password) throws Exception{
        Map<String, String> map = new HashMap<>(); // 存放返回的结果
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        try {
            Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);//登陆失败会自动处理

            UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();
            User user = loginUser.getUser();
            String jwt = JwtUtil.createJWT(user.getId().toString());
            map.put("error_message","success");
            map.put("token",jwt);
            return map;
        }catch (Exception e){
            map.put("error_message", "账号不存在，请先注册一个账号");
            return map;
        }
    }
}

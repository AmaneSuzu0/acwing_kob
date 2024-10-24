package com.kob.backend.service.user.account;

import java.util.Map;

/**
 * @author Tong Lu
 * @description:
 * @create 2024-02-27 16:43
 */
public interface LoginService {
    public Map<String,String> getToken (String username,String password) throws Exception;
}

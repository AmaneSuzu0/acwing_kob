package com.kob.backend.service.user.account;

import java.util.Map;

/**
 * @author Tong Lu
 * @description:
 * @create 2024-02-27 16:47
 */
public interface RegisterService {
    public Map<String,String> register(String username, String password,String confirmedPassword);
}

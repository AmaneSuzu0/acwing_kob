package com.kob.backend.service.ranklist;

import com.alibaba.fastjson2.JSONObject;

/**
 * @author Tong Lu
 * @description:
 * @create 2024-03-11 19:44
 */
public interface GetRankListService {
    JSONObject getList(Integer page);
}

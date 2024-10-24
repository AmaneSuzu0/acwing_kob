package com.kob.backend.service.record;

import com.alibaba.fastjson2.JSONObject;

/**
 * @author Tong Lu
 * @description:
 * @create 2024-03-11 15:57
 */
public interface GetRecordListService {
    JSONObject getList(Integer page);
}

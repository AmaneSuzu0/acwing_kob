package com.kob.backend.controller.ranklist;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.service.ranklist.GetRankListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Tong Lu
 * @description:
 * @create 2024-03-11 19:45
 */
@RestController
public class GetRankListController {
    @Autowired
    private GetRankListService getRankListService;

    @GetMapping("/api/ranklist/getlist/")
    public JSONObject getList(@RequestParam Map<String ,String> data){
        Integer page = Integer.parseInt(data.get("page"));
        return getRankListService.getList(page);
    }
}

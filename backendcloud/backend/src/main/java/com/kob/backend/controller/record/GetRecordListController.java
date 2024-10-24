package com.kob.backend.controller.record;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.service.record.GetRecordListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Tong Lu
 * @description:
 * @create 2024-03-11 15:58
 */
@RestController
public class GetRecordListController {
    @Autowired
    private GetRecordListService getRecordListService;

    @GetMapping("/api/record/getlist/")
    JSONObject getList(@RequestParam Map<String,String> data){
        Integer page = Integer.parseInt(data.get("page"));
        return  getRecordListService.getList(page);
    }

}

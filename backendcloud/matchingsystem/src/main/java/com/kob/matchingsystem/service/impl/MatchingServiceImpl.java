package com.kob.matchingsystem.service.impl;

import com.kob.matchingsystem.service.MatchingService;
import com.kob.matchingsystem.service.impl.utils.MatchingPool;
import org.springframework.stereotype.Service;

import java.util.BitSet;

/**
 * @author Tong Lu
 * @description:
 * @create 2024-03-06 15:32
 */
@Service
public class MatchingServiceImpl implements MatchingService {

    public final static MatchingPool matchingPool = new MatchingPool();

    @Override
    public String addPlayer(Integer userId, Integer rating,Integer botId) {
        System.out.println("add player: "+userId+" "+rating);
        matchingPool.addPlayer(userId,rating, botId);
        return "add player success";
    }

    @Override
    public String removePlayer(Integer userId) {
        System.out.println("remove player "+userId);
        matchingPool.removePlayer(userId);
        return "remove player success";
    }
}

package com.kob.matchingsystem.service;

/**
 * @author Tong Lu
 * @description:
 * @create 2024-03-06 15:31
 */
public interface MatchingService {
    String addPlayer(Integer userId,Integer rating,Integer botId);
    String removePlayer(Integer userId);
}

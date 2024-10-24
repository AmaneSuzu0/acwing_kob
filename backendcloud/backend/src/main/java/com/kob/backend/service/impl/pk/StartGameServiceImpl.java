package com.kob.backend.service.impl.pk;

import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.service.pk.StartGameService;
import org.springframework.stereotype.Service;

/**
 * @author Tong Lu
 * @description:
 * @create 2024-03-07 11:16
 */
@Service
public class StartGameServiceImpl implements StartGameService {
    @Override
    public String startGame(Integer aId, Integer aBotId,Integer bId,Integer bBotId) {
        System.out.println("playerA: "+aId+"playerB: "+bId);
        WebSocketServer.startGame(aId,aBotId,bId,bBotId);
        return "start game success";
    }
}

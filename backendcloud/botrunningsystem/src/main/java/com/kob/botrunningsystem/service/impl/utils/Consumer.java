package com.kob.botrunningsystem.service.impl.utils;

import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * @author Tong Lu
 * @description:
 * @create 2024-03-08 19:36
 */
//用来消耗消息队列的消费者本体
@Configuration
public class Consumer extends Thread{
    private Bot bot;
    private static RestTemplate restTemplate;

    private final static String receiveBotMoveUrl = "http://127.0.0.1:3000/pk/receive/bot/move/";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        Consumer.restTemplate = restTemplate;
    }


    public void startTimeOut(long timeout,Bot bot){
        this.bot = bot;
        this.start();

        try {
            this.join(timeout);//最多等待timeout秒，否则这个线程就直接中断
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            this.interrupt();
        }
    }


    private String addUid(String code,String uid){//在bot的code中的BotCode类名后面补上uuid
        int k = code.indexOf(" implements java.util.function.Supplier<Integer>");
        return  code.substring(0,k)+uid+code.substring(k);
    }


    @Override
    public void run() {

        UUID uuid = UUID.randomUUID();
        String uid = uuid.toString().substring(0,8);


        Supplier<Integer> botInterface = Reflect.compile(//动态执行bot的code
                "com.kob.botrunningsystem.utils.BotCode"+uid,
                addUid(bot.getBotCode(),uid)
        ).create().get();

        File file = new File("input.txt");
        try(PrintWriter fout =  new PrintWriter(file)) {
            fout.println(bot.getGameInfo());
            fout.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Integer direction = botInterface.get();

        System.out.println("move-direction: " + bot.getUserId() + " " + direction);
        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
        data.add("user_id",bot.getUserId().toString());
        data.add("direction",direction.toString());
        restTemplate.postForObject(receiveBotMoveUrl,data,String.class);

    }
}

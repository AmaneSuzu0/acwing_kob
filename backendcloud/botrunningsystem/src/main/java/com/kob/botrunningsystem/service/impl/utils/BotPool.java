package com.kob.botrunningsystem.service.impl.utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Tong Lu
 * @description:
 * @create 2024-03-08 19:23
 */
public class BotPool extends Thread{
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    private final Queue<Bot> bots = new LinkedList<>();

    public void addBot(Integer userId,String botCode,String gameInfo){
        lock.lock();
        try {
            bots.add(new Bot(userId,botCode,gameInfo));
            condition.signalAll();
        }finally {
            lock.unlock();
        }
    }

    private void consume(Bot bot){//主要来消耗掉一个从消息队列中取来的bot
        Consumer consumer = new Consumer();
        consumer.startTimeOut(2000,bot);


    }

    @Override
    public void run() {
        while (true){
            lock.lock();
            if(bots.isEmpty()){
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    lock.unlock();
                    break;
                }
            }else {
                Bot bot = bots.remove();
                lock.unlock();
                consume(bot);
            }

        }
    }
}

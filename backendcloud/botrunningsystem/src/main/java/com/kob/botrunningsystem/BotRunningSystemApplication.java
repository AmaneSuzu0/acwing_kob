package com.kob.botrunningsystem;

import com.kob.botrunningsystem.service.BotRunningService;
import com.kob.botrunningsystem.service.impl.BotRunningServiceImpl;
import com.kob.botrunningsystem.service.impl.utils.BotPool;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Tong Lu
 * @description:
 * @create ${YEAR}-${MONTH}-${DAY} ${TIME}
 */
@SpringBootApplication
public class BotRunningSystemApplication {
    public static void main(String[] args) {
        BotRunningServiceImpl.botpool.start();
        SpringApplication.run(BotRunningSystemApplication.class, args);
    }
}
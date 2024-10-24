package com.kob.matchingsystem;

import com.kob.matchingsystem.service.impl.MatchingServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Tong Lu
 * @description:
 * @create ${YEAR}-${MONTH}-${DAY} ${TIME}
 */
@SpringBootApplication
public class MatchingSystemApplication {

    public static void main(String[] args) {
        MatchingServiceImpl.matchingPool.start();
        SpringApplication.run(MatchingSystemApplication.class,args);
    }
}
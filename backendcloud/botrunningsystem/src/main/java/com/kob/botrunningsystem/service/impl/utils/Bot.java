package com.kob.botrunningsystem.service.impl.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tong Lu
 * @description:
 * @create 2024-03-08 19:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bot {
    Integer userId;
    String botCode;
    String gameInfo;
}

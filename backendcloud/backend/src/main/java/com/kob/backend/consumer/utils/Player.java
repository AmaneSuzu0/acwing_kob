package com.kob.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tong Lu
 * @description:
 * @create 2024-03-04 18:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer id;
    private Integer botId;
    private String botCode;
    private Integer sx;
    private Integer sy;
    private List<Integer> steps;

    private boolean check_tail_increasing(Integer steps) {//检查当前回合蛇的长度是否增加
        if (steps <= 5) return true;
        return steps % 3 == 1;
    }

    public List<Cell> getCells() {
        List<Cell> res = new ArrayList<>();
        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        int x = sx, y = sy;
        res.add(new Cell(x, y));
        int step = 0;
        for (int d : steps) {
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x, y));

            if (!check_tail_increasing(++step)) {
                res.remove(0);
            }
        }
        return res;
    }

    public String getStepString(){
        StringBuilder res = new StringBuilder();
        for(int d:steps) {
            res.append(d);
        }
        return res.toString();
    }

}

package com.kob.backend.consumer.utils;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;
import org.springframework.objenesis.instantiator.perc.PercInstantiator;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Tong Lu
 * @description:
 * @create 2024-03-03 16:15
 */
public class Game extends Thread{
        private final Integer rows;
        private final Integer cols;
        private final Integer inner_walls_count;
        private final int[][] g;
        private final static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};

        private final Player playerA,playerB;

        private Integer nextStepA=null;
        private Integer nextStepB=null;

        private ReentrantLock lock = new ReentrantLock();

        private String status = "playing";//playing是正在进行游戏 finished是结束了
        private String winner = "";//all是平均 A是A赢，B是B赢

        private final static String addBotCodeUrl = "http://127.0.0.1:3002/bot/add/";

        public Game(
                Integer rows,
                Integer cols,
                Integer inner_walls_count,
                Integer idA,
                Bot botA,
                Integer idB,
                Bot botB
        ) {
            this.rows = rows;
            this.cols = cols;
            this.inner_walls_count = inner_walls_count;
            this.g = new int[rows][cols];
            Integer botIdA = -1,botIdB = -1;
            String botCodeA ="",botCodeB="";

            if(botA!=null){
                botIdA=botA.getId();
                botCodeA=botA.getContent();
            }
            if(botB !=null){
                botIdB=botB.getId();
                botCodeB=botB.getContent();
            }

            this.playerA = new Player(idA,botIdA,botCodeA,rows-2,1,new ArrayList<>());

            this.playerB = new Player(idB,botIdB,botCodeB,1,cols-2,new ArrayList<>());
        }

        public Player getPlayerA(){
            return  this.playerA;
        }

        public Player getPlayerB(){
            return  this.playerB;
        }

        public void setNextStepA(Integer nextStepA){
            lock.lock();
            try {
                this.nextStepA = nextStepA;
            }finally {
                lock.unlock();
            }

        }
        public void setNextStepB(Integer nextStepB){
            lock.lock();
            try {
                this.nextStepB = nextStepB;
            }finally {
                lock.unlock();
            }

        }

        public int[][] getG() {
            return g;
        }

        private boolean check_connectivity(int sx, int sy, int tx, int ty) {
            if (sx == tx && sy == ty) return true;
            g[sx][sy] = 1;

            for (int i = 0; i < 4; i ++ ) {
                int x = sx + dx[i], y = sy + dy[i];
                if (x >= 0 && x < this.rows && y >= 0 && y < this.cols && g[x][y] == 0) {
                    if (check_connectivity(x, y, tx, ty)) {
                        g[sx][sy] = 0;
                        return true;
                    }
                }
            }
            g[sx][sy] = 0;
            return false;
        }


        private boolean draw() {  // 画地图
            for (int i = 0; i < this.rows; i ++ ) {
                for (int j = 0; j < this.cols; j ++ ) {
                    g[i][j] = 0;
                }
            }

            for (int r = 0; r < this.rows; r ++ ) {
                g[r][0] = g[r][this.cols - 1] = 1;
            }
            for (int c = 0; c < this.cols; c ++ ) {
                g[0][c] = g[this.rows - 1][c] = 1;
            }

            Random random = new Random();
            for (int i = 0; i < this.inner_walls_count / 2; i ++ ) {
                for (int j = 0; j < 1000; j ++ ) {
                    int r = random.nextInt(this.rows);
                    int c = random.nextInt(this.cols);

                    if (g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1)
                        continue;
                    if (r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2)
                        continue;

                    g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                    break;
                }
            }

            return check_connectivity(this.rows - 2, 1, 1, this.cols - 2);
        }

        public void createMap() {
            for (int i = 0; i < 1000; i ++ ) {
                if (draw())
                    break;
            }
        }

    private boolean check_valid(List<Cell> cellsA,List<Cell> cellsB){
        int n = cellsA.size();

        Cell cell = cellsA.get(n-1);//A的蛇头
        if(g[cell.x][cell.y]==1)return false;

        for(int i=0;i<n-1;i++){
            if(cellsA.get(i).x==cell.x &&cellsA.get(i).y==cell.y){
                return false;
            }
        }

        for(int i=0;i<n-1;i++){
            if(cellsB.get(i).x==cell.x && cellsB.get(i).y==cell.y){
                return false;
            }
        }
        return true;
    }

    private void judge(){//判断移动是否合法
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();

        boolean validA = check_valid(cellsA,cellsB);
        boolean validB = check_valid(cellsB,cellsA);
        if(!validA || !validB){
            status = "finished";
            if(!validA &&!validB){
                winner = "all";
            }
            else if(!validA){
                winner = "B";
            }else {
                winner = "A";
            }
        }
    }

    private void sendAllMessage(String message){//向两个client前端发送数据data的函数
        if(WebSocketServer.users.get(playerA.getId()) != null) WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        if(WebSocketServer.users.get(playerB.getId()) != null)WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }

    private void sendMove(){//向前端返回其他玩家的移动信息
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event","move");
            resp.put("a_direction",nextStepA);
            resp.put("b_direction",nextStepB);
            sendAllMessage(resp.toJSONString());
            nextStepA=nextStepB=null;
        }finally {
            lock.unlock();
        }

    }

    private String getMapString(){//把地图信息转化成字符串返回
        StringBuilder res = new StringBuilder();
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                res.append(g[i][j]);
            }
        }
        return res.toString();
    }

    private void updateUserRating(Player player,Integer rating){
        User user = WebSocketServer.userMapper.selectById(player.getId());
        user.setRating(rating);
        WebSocketServer.userMapper.updateById(user);
    }


    private void saveToDatabase(){//把对局信息存储到数据库
        Integer ratingA = WebSocketServer.userMapper.selectById(playerA.getId()).getRating();
        Integer ratingB = WebSocketServer.userMapper.selectById(playerB.getId()).getRating();

        if("A".equals(winner)){
            ratingA += 5;
            ratingB -= 3;
        }else if("B".equals(winner)){
            ratingB += 5;
            ratingA -= 5;
        }

        updateUserRating(playerA,ratingA);
        updateUserRating(playerB,ratingB);

        Record record = new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepString(),
                playerB.getStepString(),
                getMapString(),
                winner,
                new Date()
        );

        WebSocketServer.recordMapper.insert(record);
    }

    private void sendResult(){//向两个client公布结果
        JSONObject resp = new JSONObject();
        resp.put("event","result");
        resp.put("winner",winner);
        saveToDatabase();
        sendAllMessage(resp.toJSONString());
    }

        private String getGameInfo(Player player){//辅助函数：获得当前的对局信息的字符串
            Player me,you;
            if(playerA.getId().equals(player.getId())){
                me = playerA;
                you = playerB;
            }else {
                me = playerB;
                you = playerA;
            }

            return getMapString()+"#"
                    +me.getSx()+"#"
                    +me.getSy()+"#("
                    +me.getStepString()+")#"
                    +you.getSx()+"#"
                    +you.getSy()+"#("
                    +you.getStepString()+")";
        }


        private void sendBotCode(Player player){//发送bot的代码给bot-running的微服务

            if(player.getBotId().equals(-1))return;//
            MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
            data.add("user_id",player.getId().toString());
            data.add("bot_code",player.getBotCode());
            data.add("game_info",getGameInfo(player));

            WebSocketServer.restTemplate.postForObject(addBotCodeUrl,data,String.class);

        }



        private boolean nextStep(){//检查两名玩家下一步的操作是否存在
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            sendBotCode(playerA);
            sendBotCode(playerB);

            for(int i=0;i<25;i++){
                try {
                    Thread.sleep(200);
                    lock.lock();
                    try {
                        if(nextStepA !=null && nextStepB != null){
                            playerA.getSteps().add(nextStepA);
                            playerB.getSteps().add(nextStepB);
                            return true;
                        }
                    }finally {
                        lock.unlock();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            return false;
        }



        @Override
        public void run() {

            try {
                Thread.sleep(2000);//睡两秒与前端同步
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            for (int i = 0; i < 1000; i++) {
                if(nextStep()){//是否获得了两条蛇的下一步操作
                    judge();

                    if(status.equals("playing")){
                        sendMove();
                    }else {
                        sendResult();
                        break;
                    }
                }else{
                    status = "finished";
                    lock.lock();
                    try {
                        if(nextStepA==null && nextStepB==null){
                            winner = "all";
                        } else if (nextStepA==null) {
                            winner = "B";
                        } else {
                            winner = "A";
                        }
                    }finally {
                        lock.unlock();
                    }
                    sendResult();
                    break;
                }
            }

        }

}


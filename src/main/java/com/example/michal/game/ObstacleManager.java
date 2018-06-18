package com.example.michal.game;

import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;

import static com.example.michal.game.Constants.FULL_SCREEN_OBSTACLE_TRAVEL_TIME;
import static com.example.michal.game.Constants.MIN_O_GAP;
import static com.example.michal.game.Constants.MIN_P_GAP;
import static com.example.michal.game.Constants.SAFE_BUFFER_O_GAP;
import static com.example.michal.game.Constants.SAFE_BUFFER_P_GAP;
import static com.example.michal.game.Constants.SPEED_FACTOR_BASE2;
import static com.example.michal.game.Constants.SPEED_INCREASE_INTERVAL;
import static com.example.michal.game.Constants.SPEED_INCREASE_MULTIPLYER;

/**
 * Created by Michal on 08.05.2018.
 */

public class ObstacleManager {
    //higher index = lower on scrren = higher y vale
/*
    private float FULL_SCREEN_OBSTACLE_TRAVEL_TIME = Constants.FULL_SCREEN_OBSTACLE_TRAVEL_TIME;
    private float SPEED_INCREASE_INTERVAL = Constants.SPEED_INCREASE_INTERVAL;
    private float SPEED_INCREASE_MULTIPLYER = Constants.SPEED_INCREASE_MULTIPLYER;
    private float SPEED_FACTOR_BASE2 = Constants.SPEED_FACTOR_BASE2;
    private float MIN_P_GAP = Constants.MIN_P_GAP;
    private float MIN_O_GAP = Constants.MIN_O_GAP;
    private int SAFE_BUFFER_P_GAP = Constants.SAFE_BUFFER_P_GAP;
    private int SAFE_BUFFER_O_GAP = Constants.SAFE_BUFFER_O_GAP;
*/
    private float SPEED_FACTOR_BASE1 = 1.0f - SPEED_FACTOR_BASE2/(Constants.SCREEN_HEIGHT/ FULL_SCREEN_OBSTACLE_TRAVEL_TIME);
    private ArrayList<Obstacle> obstacles;
    private int playerGap;
    private int obstacleGap;
    private int obstacleHeight;
    private int color;
    private RectPlayer player;

    private long startTime;
    public long initTime;

    private long goodTime;
    private long myTime;
    float speed;

    private final int START_PLAYER_GAP;
    private final int START_OBSACLE_GAP;

    public ObstacleManager(int playerGap, int obstacleGap, int obstacleHeight, int color, RectPlayer player){

        START_OBSACLE_GAP = obstacleGap;
        START_PLAYER_GAP = playerGap;

        this.playerGap = playerGap;
        this.obstacleGap = obstacleGap;
        this.obstacleHeight = obstacleHeight;
        this.color = color;
        this.player = player;

        obstacles = new ArrayList<>();
        speed = Constants.SCREEN_HEIGHT/ FULL_SCREEN_OBSTACLE_TRAVEL_TIME;
        startTime = initTime = myTime = System.currentTimeMillis();
        populateObstacles();
    }

    public boolean playerCollide(RectPlayer player){
        for(Obstacle ob: obstacles){
            if(ob.playerCollide(player))
                return true;
        }
        return false;
    }
    int randomWithRange(int min, int max)
    {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

    private void populateObstacles(){
        int currY = -5*Constants.SCREEN_HEIGHT/4;
        while (currY < 0){
            int xStart =(int)(Math.random()*(Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(new Obstacle(obstacleHeight,color,xStart,currY,playerGap));
            currY += obstacleHeight + obstacleGap;
        }
    }
    public void update(){
        if(startTime < Constants.INIT_TIME)
            startTime = Constants.INIT_TIME;
        int elapsedTime = (int)(System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        goodTime = startTime-myTime;
        if(goodTime>SPEED_INCREASE_INTERVAL){
            speed = SPEED_INCREASE_MULTIPLYER * speed;
            myTime = System.currentTimeMillis();
        }
        for(Obstacle ob : obstacles){
            ob.incrementY(speed * elapsedTime);
        }
        if(obstacles.get(obstacles.size()-1).getRectangle().top >= Constants.SCREEN_HEIGHT){

            float speedFactor = SPEED_FACTOR_BASE1 + SPEED_FACTOR_BASE2/speed;
            float valueO = speedFactor * randomWithRange((int)(START_OBSACLE_GAP* speedFactor),START_OBSACLE_GAP);
            System.out.println("zmiana " + speedFactor);
            float value = (speedFactor)*randomWithRange((int)(START_PLAYER_GAP* speedFactor),START_PLAYER_GAP);
            playerGap += SAFE_BUFFER_P_GAP;
            obstacleGap += SAFE_BUFFER_O_GAP;
            playerGap = (int)(value <= START_PLAYER_GAP ? (value <= MIN_P_GAP*(float)START_PLAYER_GAP ? MIN_P_GAP*(float)START_PLAYER_GAP : value) : START_PLAYER_GAP);
            obstacleGap = (int)(valueO <= START_OBSACLE_GAP ? (valueO <= MIN_O_GAP*(float)START_OBSACLE_GAP ? MIN_O_GAP*(float)START_OBSACLE_GAP : valueO) : START_OBSACLE_GAP);
            System.out.println("player gap " + playerGap);
            System.out.println("obstacle gap " + obstacleGap);
            int xStart =(int)(Math.random()*(Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(0, new Obstacle(obstacleHeight, color, xStart,(obstacles.get(0).getRectangle().top - obstacleHeight - obstacleGap),playerGap));
            obstacles.remove(obstacles.size()-1);
        }

    }
    public void draw(Canvas canvas){
        for(Obstacle ob: obstacles){
            ob.checkscore(player);
            ob.draw(canvas);
        }
    }
}

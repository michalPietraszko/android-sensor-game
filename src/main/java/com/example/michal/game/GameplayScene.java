package com.example.michal.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

import static com.example.michal.game.Constants.GAME_BACKGROUND_COLOR;
import static com.example.michal.game.Constants.GAME_OBSTACLE_COLOR;
import static com.example.michal.game.Constants.GAME_OVER_COLOR;
import static com.example.michal.game.Constants.GAME_OVER_TEXT_SIZE;
import static com.example.michal.game.Constants.GAME_OVER_WAIT_TIME;
import static com.example.michal.game.Constants.OBSTACLE_GAP;
import static com.example.michal.game.Constants.OBSTACLE_HEIGHT;
import static com.example.michal.game.Constants.PLAYER_GAP;
import static com.example.michal.game.Constants.PLAYER_SIZE_X;
import static com.example.michal.game.Constants.PLAYER_SIZE_Y;
import static com.example.michal.game.Constants.X_SENSOR_ERROR_MARGIN;
import static com.example.michal.game.Constants.X_SPEED_FACTOR;
import static com.example.michal.game.Constants.Y_SENSOR_ERROR_MARGIN;
import static com.example.michal.game.Constants.Y_SPEED_FACTOR;

/**
 * Created by Michal on 10.05.2018.
 */

public class GameplayScene implements Scene{
/*
    private int PLAYER_GAP = Constants.PLAYER_GAP;
    private int OBSTACLE_GAP = Constants.OBSTACLE_GAP;
    private int PLAYER_SIZE_X = Constants.PLAYER_SIZE_X;
    private int PLAYER_SIZE_Y = Constants.PLAYER_SIZE_Y;
    private int OBSTACLE_HEIGHT = Constants.OBSTACLE_HEIGHT;
    private int GAME_OVER_WAIT_TIME = Constants.GAME_OVER_WAIT_TIME;
    private int GAME_OVER_COLOR = Constants.GAME_OVER_COLOR;
    private int GAME_OVER_TEXT_SIZE = Constants.GAME_OVER_TEXT_SIZE;
    private int GAME_BACKGROUND_COLOR = Constants.GAME_BACKGROUND_COLOR;
    private int GAME_OBSTACLE_COLOR = Constants.GAME_OBSTACLE_COLOR;
    private float X_SPEED_FACTOR = Constants.X_SPEED_FACTOR;
    private float Y_SPEED_FACTOR = Constants.Y_SPEED_FACTOR;
    private int X_SENSOR_ERROR_MARGIN = Constants.X_SENSOR_ERROR_MARGIN;
    private int Y_SENSOR_ERROR_MARGIN = Constants.Y_SENSOR_ERROR_MARGIN;
*/
    private RectPlayer player;
    private Point playerPoint;
    private ObstacleManager obstacleManager;
    private boolean alreadyDrawn = false;
    private boolean movingPlayer = false;
    private boolean gameOver = false;
    private long gameOverTime;
    //for drawing on screen
    Rect r = new Rect();

    private OrientationData orientationData;
    private long frameTime;
    private static int score = 0;
    public static int topScore = 0;
    public static void scorePoint(){
        score++;
        topScore = (topScore < score ? topScore+1 : topScore);
    }

    public GameplayScene() {
        //set player size and alien color
        player = new RectPlayer(new Rect(100,100,100 + PLAYER_SIZE_X,100 + PLAYER_SIZE_Y), 0);

        playerPoint = new Point(Constants.SCREEN_WIDTH /2,Constants.SCREEN_HEIGHT/2);
        //set player position to middle
        player.update(playerPoint);
        //create obsticles
        obstacleManager = new ObstacleManager(PLAYER_GAP, OBSTACLE_GAP, OBSTACLE_HEIGHT, GAME_OBSTACLE_COLOR,player);
        //get orientation data and time
        orientationData = new OrientationData();
        orientationData.register();
        frameTime = System.currentTimeMillis();
    }

    public void reset(){
        //set player position to middle
        playerPoint = new Point (Constants.SCREEN_WIDTH /2,Constants.SCREEN_HEIGHT /2);
        player.update(playerPoint);
        //create obsticles
        obstacleManager = new ObstacleManager(PLAYER_GAP, OBSTACLE_GAP, OBSTACLE_HEIGHT, GAME_OBSTACLE_COLOR,player);
        movingPlayer = false;
        score = 0;
    }

    @Override
    public void update() {
        //steering
        if (!gameOver) {
            if (frameTime < Constants.INIT_TIME)
                frameTime = Constants.INIT_TIME;
            int elapseTime = (int) (System.currentTimeMillis() - frameTime);
            frameTime = System.currentTimeMillis();
            if (orientationData.getOrientation() != null && orientationData.getStartOrientation() != null) {
                float pitch = orientationData.getOrientation()[1] - orientationData.getStartOrientation()[1];
                float roll = orientationData.getOrientation()[2] - orientationData.getStartOrientation()[2];

                float xSpeed = 2 * roll * Constants.SCREEN_WIDTH / X_SPEED_FACTOR;
                float ySpeed = pitch * Constants.SCREEN_HEIGHT / Y_SPEED_FACTOR;

                playerPoint.x += Math.abs(xSpeed * elapseTime) > X_SENSOR_ERROR_MARGIN ? xSpeed * elapseTime : 0;
                playerPoint.y -= Math.abs(ySpeed * elapseTime) > Y_SENSOR_ERROR_MARGIN ? ySpeed * elapseTime : 0;
            }
            //player can be half outside screen
            if (playerPoint.x < 0) {
                playerPoint.x = 0;
            } else if (playerPoint.x > Constants.SCREEN_WIDTH) {
                playerPoint.x = Constants.SCREEN_WIDTH;
            }
            if (playerPoint.y < 0) {
                playerPoint.y = 0;
            } else if (playerPoint.y > Constants.SCREEN_HEIGHT) {
                playerPoint.y = Constants.SCREEN_HEIGHT;
            }

            player.update(playerPoint);
            obstacleManager.update();

            if (obstacleManager.playerCollide(player)) {
                gameOver = true;//?? ponizej
                gameOverTime = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {

        canvas.drawColor(GAME_BACKGROUND_COLOR);
        player.draw(canvas);
        obstacleManager.draw(canvas);

        Paint paint1 = new Paint();
        paint1.setTextSize(GAME_OVER_TEXT_SIZE);
        paint1.setColor(GAME_OVER_COLOR);
        drawScore(canvas, paint1, Integer.toString(score));
        drawTopScore(canvas, paint1, Integer.toString(topScore));

        //game over handling
        if(gameOver){
            Paint paint = new Paint();
            paint.setTextSize(GAME_OVER_TEXT_SIZE);
            paint.setColor(GAME_OVER_COLOR);
            drawCenter(canvas, paint, "Game Over");
        }
    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        //return super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //right now taping on screen will reset your orientation data
               // playerPoint = new Point (Constants.SCREEN_WIDTH /2,Constants.SCREEN_HEIGHT /2);
               // player.update(playerPoint);
                orientationData = new OrientationData();
                orientationData.register();

                if(!gameOver && player.getRectangle().contains((int)event.getX(),(int)event.getY()))
                    movingPlayer = true;
                if(gameOver && System.currentTimeMillis() - gameOverTime >= GAME_OVER_WAIT_TIME) {
                    reset();
                    gameOver = false;
                    orientationData.newGame();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(!gameOver && movingPlayer)
                    playerPoint.set((int)event.getX(),(int)event.getY());
                break;
            case MotionEvent.ACTION_UP:
                movingPlayer = false;
                break;
        }
    }
    private void drawCenter(Canvas canvas, Paint paint, String text){
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint );
    }
    private void drawScore(Canvas canvas, Paint paint, String text){
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = 100f;
        float y = 100f;
        canvas.drawText(text, x, y, paint );
    }
    private void drawTopScore(Canvas canvas, Paint paint, String text){
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = Constants.SCREEN_WIDTH - 200f;
        float y = 100f;
        canvas.drawText(text, x, y, paint );
    }


}

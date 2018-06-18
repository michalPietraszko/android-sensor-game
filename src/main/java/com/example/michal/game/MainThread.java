package com.example.michal.game;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

import static com.example.michal.game.Constants.MAX_FPS;


/**
 * Created by Michal on 08.05.2018.
 */

public class MainThread extends Thread {



    public static double averageFPS = 0;

    private GamePanel gamePanel;

    private SurfaceHolder surfaceHolder;

    private boolean running;

    public static Canvas canvas;
    public Context context;

    public void setRunning(boolean running){
        this.running = running;
    }

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel, Context c){
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
        context = c;
    }

    @Override
    public void run(){
        long startTime;
        long timeMillis = 1000/MAX_FPS;
        long waitTime;
        int frameCount = 0;
        long totalTime = 0;
        long targetTime = 1000/MAX_FPS;

        while (running){
            startTime = System.nanoTime();
            canvas = null;

            try{
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                if(canvas != null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }catch (Exception e ){e.printStackTrace();}
                }
            }
            timeMillis = (System.nanoTime() - startTime)/ 1000000;
            waitTime = targetTime - timeMillis;
            try{
                if(waitTime > 0)
                    this.sleep(waitTime);
                gamePanel.saveData(context);
            }catch (Exception e ){e.printStackTrace();}

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if(frameCount == MAX_FPS){
                 averageFPS = 1000/((totalTime/frameCount)/1000000);
                 frameCount = 0;
                 totalTime = 0;
                 //System.out.println(averageFPS);
            }
        }
    }
}

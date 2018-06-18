package com.example.michal.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;




import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Michal on 08.05.2018.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    public static final String SHARED_PREFS = "sharedPrefs";
    private MainThread thread;
    public SharedPreferences sp;
    private  SceneManager manager;

    public void saveData(Context context){
        int i = GameplayScene.topScore;
        //sp = context.getSharedPreferences("highest_score",MODE_PRIVATE);
        SharedPreferences.Editor spe = sp.edit();
        spe.putInt("highest_score", i);
        spe.commit();

    }
    public int loadData(Context context){
        SharedPreferences sp = context.getSharedPreferences("highest_score",MODE_PRIVATE);
        SharedPreferences.Editor spe = sp.edit();
        int nru = sp.getInt("highest_score", 0);
        return nru;
    }
    public GamePanel(Context context){
        super(context);
        getHolder().addCallback(this);
        sp = context.getSharedPreferences("highest_score",MODE_PRIVATE);
        Constants.CURRENT_CONTEXT = context;
        thread = new MainThread(getHolder(), this, getContext());
        manager = new SceneManager();
        setFocusable(true);
        GameplayScene.topScore = loadData(getContext());
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this, getContext());
        Constants.INIT_TIME = System.currentTimeMillis();
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while(retry) {//mistake?
            try {
                thread.setRunning(false);
                thread.join();
            }catch (Exception e){   e.printStackTrace();   }
            retry = false;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        manager.receiveTouch(event);
        return true;
    }
    public void update() {
        manager.update();
    }
    @Override
    public void draw (Canvas canvas){
        super.draw(canvas);
        manager.draw(canvas);
    }

}

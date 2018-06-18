package com.example.michal.game;

import android.animation.AnimatorInflater;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Michal on 10.05.2018.
 */

public class Animation {

    private Bitmap[] frames;
    private int frameIndex;

    private boolean isPlaying = false;

    private float frameTime;
    private long lastFrame;
    private boolean alreadyScaled = false;

    public boolean isPlaying(){
        return isPlaying;
    }
    public void play(){
        isPlaying = true;
        frameIndex = 0;
        lastFrame = System.currentTimeMillis();
    }
    public void stop(){
        isPlaying = false;

    }
    public void draw(Canvas canvas, Rect destination){
        if(!isPlaying)
            return;
        if(!alreadyScaled)
            scaleRect(destination);

        canvas.drawBitmap(frames[frameIndex], null, destination, new Paint());

    }

    private void scaleRect(Rect rect) {
        float whRatio =  (float)(frames[frameIndex].getWidth())/frames[frameIndex].getHeight();
        if(rect.width()>rect.height())
            rect.left = rect.right - (int)(rect.height()*whRatio);
        else
            rect.top = rect.bottom - (int)(rect.width()*(1/whRatio));
        alreadyScaled = true;
    }



    public void update(){

        //if(!isPlaying)
       //     return;

        if(System.currentTimeMillis() - lastFrame > frameTime*1000){
            frameIndex++;
            frameIndex = frameIndex >= frames.length ? 0 : frameIndex;
            lastFrame = System.currentTimeMillis();
        }
        //lastFrame = System.currentTimeMillis();
    }

    public Animation(Bitmap[] frames, float animTime){
        this.frames = frames;
        frameIndex = 0;
        frameTime = animTime/frames.length;
    }
}

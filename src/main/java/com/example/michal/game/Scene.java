package com.example.michal.game;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by Michal on 10.05.2018.
 */

public interface Scene {
    public void update();
    public void draw(Canvas canvas);
    public void terminate();
    public void receiveTouch(MotionEvent event);
}

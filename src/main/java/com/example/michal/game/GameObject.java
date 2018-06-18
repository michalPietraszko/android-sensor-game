package com.example.michal.game;

import android.graphics.Canvas;

/**
 * Created by Michal on 08.05.2018.
 */

public interface GameObject {
    public void draw(Canvas canvas);
    public void update();
}

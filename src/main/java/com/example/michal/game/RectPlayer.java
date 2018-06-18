package com.example.michal.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;

import static com.example.michal.game.Constants.ERROR_MARGIN_ANIMATION;
import static com.example.michal.game.Constants.ILDE_ANIMATION_INDEX;
import static com.example.michal.game.Constants.WALK_ANIM_TIME;
import static com.example.michal.game.Constants.WALK_LEFT_ANIMATION_INDEX;
import static com.example.michal.game.Constants.WALK_RIGHT_ANIMATION_INDEX;
import static com.example.michal.game.Constants.YELLOW_ALIEN;

/**
 * Created by Michal on 08.05.2018.
 */

public class RectPlayer implements GameObject {
/*
    private final float WALK_ANIM_TIME = Constants.WALK_ANIM_TIME;
    private final int ERROR_MARGIN_ANIMATION = Constants.ERROR_MARGIN_ANIMATION;
    private final int ILDE_ANIMATION_INDEX = Constants.ILDE_ANIMATION_INDEX;
    private final int WALK_RIGHT_ANIMATION_INDEX = Constants.WALK_RIGHT_ANIMATION_INDEX;
    private final int WALK_LEFT_ANIMATION_INDEX = Constants.WALK_LEFT_ANIMATION_INDEX;
    private final int YELLOW_ALIEN = Constants.YELLOW_ALIEN;
*/
    private Rect rectangle;
    private int color;
    private int state = 0;

    //Animation Manager handles player rendering
    private AnimationManager animManager;

    public RectPlayer(Rect rectangle, int color){
        this.rectangle = rectangle;
        this.color = color;
        setUpAnimations();
    }

    private void setUpAnimations(){

        Bitmap ildeImg = null; Bitmap walk1 = null; Bitmap walk2 = null;

        if(color == YELLOW_ALIEN){
            ildeImg = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienyellow);
            walk1 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienyellow_walk1);
            walk2 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienyellow_walk2);
        }

        Animation ilde = new Animation(new Bitmap[]{ildeImg}, 1.0f);
        Animation walkRight = new Animation(new Bitmap[]{walk1, walk2}, WALK_ANIM_TIME);

        Matrix m = new Matrix();
        m.preScale(-1,1);
        Bitmap walk3 = Bitmap.createBitmap(walk1,0,0,walk1.getWidth(),walk1.getHeight(),m,false);
        Bitmap walk4 = Bitmap.createBitmap(walk2,0,0,walk2.getWidth(),walk2.getHeight(),m,false);

        Animation walkLeft = new Animation(new Bitmap[]{walk3, walk4}, WALK_ANIM_TIME);

        /////////////////////////////////
        ///////ORDER OF ANIMATIONS///////
        animManager = new AnimationManager(new Animation[]{ilde, walkRight, walkLeft});
        ////////////////////////////////
        ////////////////////////////////
    }

    public Rect getRectangle() {
        return rectangle;
    }

    public int getColor() {
        return color;
    }

    @Override
    public void draw(Canvas canvas) {
       // Paint paint = new Paint();
       // paint.setColor(color );
       // canvas.drawRect(rectangle, paint);

        animManager.draw(canvas,rectangle);
    }
    //not used!
    @Override
    public void update() {
        animManager.update();
    }

    public void update(Point point){
        float oldLeft = rectangle.left;
        //l,t,r,b change player position
        rectangle.set(point.x - rectangle.width()/2, point.y - rectangle.height()/2,
                point.x + rectangle.width()/2, point.y + rectangle.height()/2);
        //remember last state
        int lastState = state;

        state = ILDE_ANIMATION_INDEX;

        if(rectangle.left - oldLeft > ERROR_MARGIN_ANIMATION)
            state = WALK_RIGHT_ANIMATION_INDEX;

        else if(rectangle.left - oldLeft < - ERROR_MARGIN_ANIMATION)
            state = WALK_LEFT_ANIMATION_INDEX;

        if(lastState != state)
            animManager.playAnim(state);

        animManager.update();
    }
}

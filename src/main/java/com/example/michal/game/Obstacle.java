package com.example.michal.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;

import static com.example.michal.game.Constants.OBSTACLE_HEIGHT;

/**
 * Created by Michal on 08.05.2018.
 */

public class Obstacle implements GameObject {

    private Rect rectangle;
    boolean alreadyScored = false;
    private int color;
    private Rect rectangle2;

    public void incrementY(float y){
        rectangle.top += y;
        rectangle.bottom += y;
        rectangle2.top += y;
        rectangle2.bottom +=y;
    }

    public Obstacle(int rectHeight, int color, int startX,int startY, int playerGap){
        rectangle = new Rect(0, startY,startX,startY+ rectHeight);
        this.color = color;
        rectangle2 = new Rect(startX + playerGap, startY, Constants.SCREEN_WIDTH, startY + rectHeight);
    }

    public Rect getRectangle() {
        return rectangle;
    }

    public boolean playerCollide(RectPlayer player){
        return Rect.intersects(rectangle, player.getRectangle()) || Rect.intersects(rectangle2, player.getRectangle());
    }

    @Override
    public void draw(Canvas canvas) {

        //resize bitmap
        Bitmap patterMid = getResizedBitmap(BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.grassmid),
                OBSTACLE_HEIGHT, OBSTACLE_HEIGHT);
        Bitmap patternEndLeftRect = getResizedBitmap(BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.grassright),
                OBSTACLE_HEIGHT, OBSTACLE_HEIGHT);
        Bitmap patternEndRightRect = getResizedBitmap(BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.grassleft),
                OBSTACLE_HEIGHT, OBSTACLE_HEIGHT);

        //Bitmap paternLeft = mergeMultiple(new Bitmap[]{patterMid, patternEndLeftRect}, rectangle, true);
        //Bitmap patternRight = mergeMultiple(new Bitmap[]{patternEndRightRect, patterMid}, rectangle2, false);

        //how many full will fit 1 one
      //  int leftHowMany = (int)(rectangle.width()/OBSTACLE_HEIGHT);
        //System.out.println("how many full = " + leftHowMany);
        //float leftLeftover = rectangle.width() - (float)leftHowMany*OBSTACLE_HEIGHT;
        //System.out.println(leftLeftover);

        //how many full will fit 2 one
        int rightHowMany = (int)(rectangle2.width()/OBSTACLE_HEIGHT);
        float rightLeftover = rectangle2.width() - (float)rightHowMany;
        //create texture for left one

        //Bitmap test = combineImagesLeft(patterMid, patternEndLeftRect, leftHowMany, leftLeftover);

        //canvas.drawBitmap(patterMid, null, rectangle, null);
       // canvas.drawBitmap(patterMid, new Rect(0,0,100,100)   , rectangle2, new Paint());
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        canvas.drawRect(rectangle, p);
        canvas.drawRect(rectangle2, p);
    }
    public Bitmap combineImagesLeft(Bitmap c, Bitmap s, int howManyFull, float leftover ) { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom
        Bitmap cs = null;

        float width, height = 0;

        width = howManyFull * c.getWidth() + leftover;
        height = c.getHeight();

        //System.out.println("width " + width);

        cs = Bitmap.createBitmap((int)width, (int)height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(cs);
        comboImage.drawBitmap(s, (howManyFull-1)*c.getWidth() + leftover, 0f, null);
        for(int i = 2; i<howManyFull; i++)
            comboImage.drawBitmap(c, (howManyFull-i)*c.getWidth() + leftover, 0f, null);

        //crop to fit rest
        Bitmap resizedbitmap=Bitmap.createBitmap(c, 0,0,(int)leftover, (int)height);
        //flip
        Matrix m = new Matrix();
        m.preScale(-1,1);
        Bitmap finalPart = Bitmap.createBitmap(resizedbitmap,0,0,resizedbitmap.getWidth(),resizedbitmap.getHeight(),m,false);

        comboImage.drawBitmap(c, 0f, 0f, null);

        return cs;
    }

    private Bitmap mergeMultiple(Bitmap[] parts, Rect rect, boolean isLeft){

        float rectWidth = rect.width();
        float rectHeight = rect.height();

        Bitmap result = Bitmap.createBitmap(parts[0].getWidth() , (int)rectHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        float left = 0f;
        float top = 0f;
        if(isLeft) {
            canvas.drawBitmap(parts[1],rectWidth - parts[0].getWidth(),top, paint);
            int n = 2;
            while(rectWidth - n*parts[0].getWidth() >= 0 ){
                canvas.drawBitmap(parts[0],rectWidth - n*parts[0].getWidth(),top, paint);
                n++;
            }

        }else{
            canvas.drawBitmap(parts[0],left,top, paint);
            int n = 1;
            while(rectWidth - n*parts[1].getWidth() >= 0 ){
                canvas.drawBitmap(parts[1], n*parts[0].getWidth(),top, paint);
                n++;
            }
        }
        return result;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    @Override
    public void update() {

    }
    public void checkscore(RectPlayer player){
        int playerY = player.getRectangle().centerY();
        int obstacleY = rectangle.centerY();
        if(!alreadyScored){
            if(playerY <= obstacleY) {
                GameplayScene.scorePoint();
                alreadyScored = true;
            }
        }
    }
}

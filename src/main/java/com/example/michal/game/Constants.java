package com.example.michal.game;

import android.content.Context;
import android.graphics.Color;

/**
 * Created by Michal on 08.05.2018.
 */

public abstract class Constants {
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static Context CURRENT_CONTEXT;
    public static long INIT_TIME;

    public static int MAX_FPS = 62;

    //GAMEPLAY SCENE//
    public static int PLAYER_GAP = 450;
    public static int OBSTACLE_GAP = 800;
    public static int PLAYER_SIZE_X = 100;
    public static int PLAYER_SIZE_Y = 100;
    public static int OBSTACLE_HEIGHT = 100;
    public static int GAME_OVER_WAIT_TIME = 2000;
    public static int GAME_OVER_COLOR = Color.MAGENTA;
    public static int GAME_OVER_TEXT_SIZE = 100;
    public static int GAME_BACKGROUND_COLOR = Color.BLACK;
    public static int GAME_OBSTACLE_COLOR = Color.WHITE;
    public static float X_SPEED_FACTOR = 500f;
    public static float Y_SPEED_FACTOR = 800f;
    public static int X_SENSOR_ERROR_MARGIN = 2;
    public static int Y_SENSOR_ERROR_MARGIN = 2;
    //RECTANGLE PLAYER//
    public static float WALK_ANIM_TIME = 0.2f;
    public static int ERROR_MARGIN_ANIMATION = 4;
    public static int ILDE_ANIMATION_INDEX = 0;
    public static int WALK_RIGHT_ANIMATION_INDEX = 1;
    public static int WALK_LEFT_ANIMATION_INDEX = 2;
    public static int YELLOW_ALIEN = 0;
    //OBSTACLE MANAGER//
    public static float FULL_SCREEN_OBSTACLE_TRAVEL_TIME = 4000.0f;
    public static float SPEED_INCREASE_INTERVAL = 1618.0f;
    public static float SPEED_INCREASE_MULTIPLYER = 1.01618f;
    public static float SPEED_FACTOR_BASE2 = 0.161803f;
    public static float MIN_P_GAP = 0.6f;
    public static float MIN_O_GAP = 0.75f;
    public static int SAFE_BUFFER_P_GAP = 100;
    public static int SAFE_BUFFER_O_GAP = 100;
}

package com.bgrummitt.tetris.view.GameActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.bgrummitt.tetris.controller.Game.Tetris;
import com.bgrummitt.tetris.controller.Other.SwipeGestureDetection;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = GameView.class.getSimpleName();

    // Global
    private GestureDetectorCompat gestureDetectorCompat;
    private GameThread mainThread;
    private Tetris game;

    /**
     * Constructor
     *
     * @param context where the activity intent was started
     */
    public GameView(Context context) {
        super(context);

        // Create a common gesture listener object.
        SwipeGestureDetection gestureListener = new SwipeGestureDetection(this);

        // Create the gesture detector with the gesture listener.
        gestureDetectorCompat = new GestureDetectorCompat(context, gestureListener);

        //This allows you intercept events
        getHolder().addCallback(this);
    }

    /**
     * This is called immediately after the surface is first created.
     * Implementations of this should start up whatever rendering code
     * they desire.  Note that only one thread can ever draw into
     * a {@link Surface}, so you should not draw into the Surface here
     * if your normal rendering will be in another thread.
     *
     * @param holder The SurfaceHolder whose surface is being created.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //Create new thread class with the SurfaceHolder and context
        mainThread = new GameThread(getHolder(), this);
        //Keep the inputs on this thread and not on the new one
        setFocusable(true);

        game = new Tetris(getResources());

        //Start the games update infinite loop
        mainThread.setRunning(true);
        mainThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    /**
     * This is called immediately before a surface is being destroyed. After
     * returning from this call, you should no longer try to access this
     * surface.  If you have a rendering thread that directly accesses
     * the surface, you must ensure that thread is no longer touching the
     * Surface before returning from this function.
     *
     * @param holder The SurfaceHolder whose surface is being destroyed.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //To stop the thread it may take a few attempts so we create a while loop
        boolean retry = true;
        while (retry) {
            try {
                mainThread.setRunning(false);
                mainThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    /**
     * Update function for updating the game every loop
     */
    public void update() {
        game.update();
    }

    /**
     * Override on touch event to intercept any screen interaction that takes place
     *
     * @param event the event that takes place
     * @return the super so we don't get another event on release
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Pass activity on touch event to the gesture detector.
        gestureDetectorCompat.onTouchEvent(event);
        // Return true to tell android OS that event has been consumed, do not pass it to other event listeners.
        return true;
    }

    /**
     * Where any the game's graphics are drawn
     *
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        //If the canvas is not empty draw the game
        if (canvas != null) {
            canvas.drawColor(Color.BLACK);
            game.draw(canvas);
        }
    }

    public void rotate(){
        game.rotateFallingBlock();
    }

    public void swipe(SwipeGestureDetection.swipeType type) {
        game.swipe(type);
    }

    public void startGame(){
        game.startGame();
    }

}

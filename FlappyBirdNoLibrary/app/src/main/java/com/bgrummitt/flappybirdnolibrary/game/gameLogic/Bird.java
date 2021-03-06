package com.bgrummitt.flappybirdnolibrary.game.gameLogic;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

public class Bird {

    final static private String TAG = Bird.class.getSimpleName();
    final static private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    final static private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    final private int jumpSpeed;
    final private int gravity;
    private int frame;
    private Bitmap birdImage;
    private Bitmap birdImageFlap;
    private int x;
    private int y;
    private boolean isInJump = false;
    private boolean birdPic = true;
    private boolean birdIsFacingDown;
    private int birdJumpDistance;
    private int birdFallDistance;
    private float percentageMoved;
    private long fallStartTime;
    private float percentagePassed;

    /**
     * Bird constructor
     * @param bird1 The 1st bird pic
     * @param bird2 The 2nd bird pic
     */
    public Bird(Bitmap bird1, Bitmap bird2){
        birdImage = bird1;
        birdImageFlap = bird2;
        //Set x and y to be half the screen and move it to the center of the Bitmap
        x = screenWidth / 2 - (bird1.getWidth() / 2);
        y = screenHeight / 2 - (bird1.getHeight() / 2);
        //Set the speed of the fall and the speed of the flap
        jumpSpeed = (screenHeight / -96);
        gravity = (screenHeight / 192);
        //Get 10% of the screen height
        birdJumpDistance = screenHeight / 10;
        //Get 5% of the screen height
        birdFallDistance = (screenHeight / 20);
        birdIsFacingDown = true;
    }

    private long startTime;

    public void update(){
//        Log.d(TAG, String.format("X = %d, Y = %d", x, y));
        //If the bird has been flapping for more than 300 milliseconds = 0.3 seconds
        if(isInJump){
            //Get the percentage of time that has passed in a form of 0.0
            percentagePassed = (System.currentTimeMillis() - startTime) / 250.0f;
            //Get the percentage of time that has passed since the last measurement and move the bird that percentage of the jump distance
            y -= ((percentagePassed - percentageMoved) * birdJumpDistance);
            //Set the percentage that the bird has moved on its upward journey
            percentageMoved = percentagePassed;
            //If it has moved 100% of its journey stop the jump
            if(percentageMoved >= 1){
                isInJump = false;
                startTime = System.currentTimeMillis();
                fallStartTime = System.currentTimeMillis();
                percentageMoved = 0;
            }
        }else if(fallStartTime != 0){
            if(!birdIsFacingDown && System.currentTimeMillis() - startTime > 100){
                birdIsFacingDown = true;
                birdImage = RotateBitmap(birdImage, 90);
                birdImageFlap = RotateBitmap(birdImageFlap, 90);
            }
            percentagePassed = (System.currentTimeMillis() - fallStartTime) / 250.0f;
            y += ((percentagePassed - percentageMoved) * birdFallDistance);
            percentageMoved = percentagePassed;
            if(percentageMoved >= 1){
                fallStartTime = System.currentTimeMillis();
                percentageMoved = 0;
            }
        }
        //Flip the birdPic boolean
        if(frame > 10) {
            birdPic = !birdPic;
            frame = 0;
        }else{
            frame++;
        }
    }

    public void flap(){
        //If the bird is not in mid flap
        if(!isInJump) {
            startTime = System.currentTimeMillis();
            percentageMoved = 0;
            isInJump = true;
            if(birdIsFacingDown) {
                birdIsFacingDown = false;
                birdImage = RotateBitmap(birdImage, 270);
                birdImageFlap = RotateBitmap(birdImageFlap, 270);
            }
        }
    }

    public void startFlapping(){
        fallStartTime = System.currentTimeMillis();
        birdImage = RotateBitmap(birdImage, 45);
        birdImageFlap = RotateBitmap(birdImageFlap, 45);
    }

    public void draw(Canvas canvas){
        //Draw the bird and depending on birdPic boolean draw birdImage or birdImageFlap
        canvas.drawBitmap(birdPic ? birdImage : birdImageFlap, x, y, null);
    }

    /**
     * Function to rotate the bitmap a set number of degrees
     * @param source the image to rotate
     * @param angle the angle at which to rotate
     * @return the new bitmap rotated to the angle
     */
    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

}

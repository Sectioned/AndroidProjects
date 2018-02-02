package com.example.bengr.project1test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Animation translatebu;
    TextView TextHW;
    TextView Textsomething;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        addAnimation();
    }

    public void addAnimation(){
        TextHW = findViewById(R.id.TextHW);
        Textsomething = findViewById(R.id.TextHW);

        translatebu= AnimationUtils.loadAnimation(this, R.anim.animationfile);
        TextHW.setText(R.string.test);
        Textsomething.setText("test");
        TextHW.startAnimation(translatebu);
        Textsomething.startAnimation(translatebu);
    }

    public void endAnimation(View pView){
        if(count == 0){
            System.out.println("Finish");
            TextHW.setText(R.string.finish);
            TextHW.clearAnimation();
            count++;
        }
    }

}

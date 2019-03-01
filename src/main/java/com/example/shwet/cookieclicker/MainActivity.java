package com.example.shwet.cookieclicker;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import static android.graphics.Color.*;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout constraintLayout;
    TextView textScore, reqt;
    ImageView cookie, grandma;
    int score=0;
    int numGma = 25;
    boolean startGma = true;
    double hor = 0;
    ScaleAnimation enter = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation leave = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        constraintLayout = findViewById(R.id.layout);
        cookie = findViewById(R.id.cookie);
        grandma =  findViewById(R.id.grandma);
        textScore = findViewById(R.id.score);
        reqt = findViewById(R.id.reqt);
        enter.setDuration(300);
        leave.setDuration(300);
        grandma.setVisibility(View.INVISIBLE);
        grandma.setClickable(false);

        final ScaleAnimation scaleanimation = new ScaleAnimation(1.0f, 0.95f, 1.0f, 0.95f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleanimation.setDuration(200);

        cookie.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.startAnimation(scaleanimation);
                startCookie();
            }
        });


        grandma.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                score-= numGma;
                numGma+=20;
                if (score >= numGma){
                    grandma.setClickable(true);
                    grandma.setVisibility(View.VISIBLE);
                }
                else {
                    grandma.startAnimation(leave);
                    grandma.setClickable(false);
                    grandma.setVisibility(View.INVISIBLE);
                    startGma = true;
                }
                textScore.setText(score + " cookies");
                placeGma(hor);
                hor+=0.20;
                new grandmaBaking().start();
            }
        });

        }
    public void startCookie() {
        final TextView addOne;
        addOne = new TextView(this);
        addOne.setId(View.generateViewId());
        addOne.setText("+1");
        addOne.setTextColor(parseColor("Black"));
        score++;
        textScore.setText(score + " cookies");

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        addOne.setLayoutParams(params);
        constraintLayout.addView(addOne);
        final ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        constraintSet.connect(addOne.getId(), ConstraintSet.TOP, textScore.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(addOne.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(addOne.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT);
        constraintSet.connect(addOne.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT);

        constraintSet.setHorizontalBias(addOne.getId(), 0.5f);
        constraintSet.setVerticalBias(addOne.getId(), 0.5f);
        constraintSet.applyTo(constraintLayout);

        TranslateAnimation translate = new TranslateAnimation(((float)Math.random()*25f)+5, ((float)Math.random()*25f)+5, 0f, -250f);
        translate.setDuration(800);
        addOne.startAnimation(translate);
        translate.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {}
            public void onAnimationEnd(Animation animation) {
                constraintLayout.removeView(addOne);
            }
            public void onAnimationRepeat(Animation animation) {}
        });
        if (startGma && score >= numGma){
            startGma = false;
            grandma.setVisibility(View.VISIBLE);
            grandma.setClickable(true);
            grandma.startAnimation(enter);
        }
    }

    public void placeGma(double hor){
        ImageView newGma = new ImageView(this);
        newGma.setImageResource(R.drawable.grandma);
        newGma.setId(View.generateViewId());
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        newGma.setLayoutParams(params);
        constraintLayout.addView(newGma);
        ConstraintSet constraintSet;
        constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(newGma.getId(), ConstraintSet.TOP, textScore.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(newGma.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(newGma.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT);
        constraintSet.connect(newGma.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT);
        constraintSet.setHorizontalBias(newGma.getId(), (float)hor);
        constraintSet.setVerticalBias(newGma.getId(), 1.0f);
        constraintSet.applyTo(constraintLayout);
    }

    public synchronized void addScore(int x) {
        score += x;
    }

    public class grandmaBaking extends Thread {
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1300);
                } catch (Exception e) {
                }
                addScore(1);
                if (score >= numGma){
                    grandma.post(new Runnable() {
                        public void run() {
                            grandma.setVisibility(View.VISIBLE);
                            grandma.setClickable(true);
                            if (startGma)
                                grandma.startAnimation(enter);
                            startGma = false;
                        }
                    });
                }
                textScore.post(new Runnable() {
                    public void run() {
                        textScore.setText(score + " cookies");
                    }
                });
            }
        }
    }
}

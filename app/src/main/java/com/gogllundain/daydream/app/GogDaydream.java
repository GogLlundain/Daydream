package com.gogllundain.daydream.app;

import java.util.Random;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.graphics.Color;
import android.graphics.Point;
import android.service.dreams.DreamService;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;

/**
 * Created by gareth on 09/04/14.
 */
public class GogDaydream extends DreamService implements OnClickListener {

    private Button dismissButton;
    private ImageView[] robotImages;
    private AnimatorSet[] robotSets;

    private final int ROW_COLS = 5;
    private final int NUMBER_OF_ROBOTS = ROW_COLS * ROW_COLS;
    private int stopButtonPosition;

    @Override
    public void onDreamingStarted() {
        // Daydream started
        super.onDreamingStarted();

        for (int robot = 0; robot < NUMBER_OF_ROBOTS; robot++) {
            if (robot != stopButtonPosition) {
                robotSets[robot].start();
            }
        }
    }

    @Override
    public void onDreamingStopped() {
        // Daydream stopped
        for (int robot = 0; robot < NUMBER_OF_ROBOTS; robot++) {
            if (robot != stopButtonPosition) {
                robotSets[robot].cancel();
            }
        }

        super.onDreamingStopped();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        setInteractive(true);
        setFullscreen(true);

        stopButtonPosition = new Random().nextInt(NUMBER_OF_ROBOTS);

        GridLayout daydreamLayout = new GridLayout(this);
        daydreamLayout.setColumnCount(ROW_COLS);
        daydreamLayout.setRowCount(ROW_COLS);
        robotSets = new AnimatorSet[NUMBER_OF_ROBOTS];
        robotImages = new ImageView[NUMBER_OF_ROBOTS];

        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        int robotWidth = screenSize.x / ROW_COLS;
        int robotHeight = screenSize.y / ROW_COLS;

        for (int robotPosition = 0; robotPosition < NUMBER_OF_ROBOTS; robotPosition++) {
            GridLayout.LayoutParams daydreamLayoutParameter = new GridLayout.LayoutParams();
            daydreamLayoutParameter.width = robotWidth;
            daydreamLayoutParameter.height = robotHeight;

            if (robotPosition == stopButtonPosition) {
                // Add a stop button
                dismissButton = new Button(this);
                dismissButton.setText("Stop");
                dismissButton.setBackgroundColor(Color.WHITE);
                dismissButton.setTextColor(Color.RED);
                dismissButton.setOnClickListener(this);
                dismissButton.setLayoutParams(daydreamLayoutParameter);
                daydreamLayout.addView(dismissButton);
            } else {
                // Add a robot
                robotImages[robotPosition] = new ImageView(this);
                robotImages[robotPosition].setImageResource(R.drawable.ic_launcher);
                daydreamLayout.addView(robotImages[robotPosition], daydreamLayoutParameter);

                robotSets[robotPosition] = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.android_spin);
                robotSets[robotPosition].setDuration(new Random().nextInt(2500) + 7500);

                robotSets[robotPosition].setTarget(robotImages[robotPosition]);
                robotImages[robotPosition].setOnClickListener(this);
            }
        }

        setContentView(daydreamLayout);
    }

    @Override
    public void onDetachedFromWindow() {
        // Tidy up

        for (int robot = 0; robot < NUMBER_OF_ROBOTS; robot++) {
            if (robot != stopButtonPosition) {
                robotImages[robot].setOnClickListener(null);
            }
        }

        super.onDetachedFromWindow();
    }

    public void onClick(View v) {
        if (v instanceof Button && (Button) v == dismissButton) {
            // Stop button was pressed
            this.finish();
        } else {
            // A robot was pressed.

            for (int robot = 0; robot < NUMBER_OF_ROBOTS; robot++) {
                if (robot == stopButtonPosition) {
                    break;
                }

                if ((ImageView) v == robotImages[robot]) {
                    if (robotSets[robot].isStarted()) {
                        robotSets[robot].cancel();
                    } else {
                        robotSets[robot].start();
                    }
                    break;
                }
            }
        }
    }
}

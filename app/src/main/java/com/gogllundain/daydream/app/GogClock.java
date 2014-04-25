package com.gogllundain.daydream.app;

import android.os.Handler;
import android.os.Looper;
import android.service.dreams.DreamService;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by gareth on 24/04/14.
 */
public class GogClock extends DreamService implements Runnable {

    private Handler updateTick = new Handler(Looper.getMainLooper());
    private int updateDelay = 1000;

    @Override
    public void onDreamingStarted()
    {
        super.onDreamingStarted();

        setTheDateAndTime();
        updateTick.postDelayed(this, updateDelay);
    }

    @Override
    public void onDreamingStopped()
    {
        super.onDreamingStopped();
    }

    @Override
    public void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        setFullscreen(true);

        setContentView(R.layout.clock_dream);
    }

    private void setTheDateAndTime() {
        try {
            TextView clockFace = (TextView) findViewById(R.id.ClockFace);
            if (clockFace != null) {
                DateFormat outputFormat = new SimpleDateFormat("HH:mm");
                clockFace.setText(outputFormat.format(new Date()));
            }

            TextView dateInfo = (TextView) findViewById(R.id.date);
            if (dateInfo != null) {
                DateFormat outputFormat = new SimpleDateFormat("EEEE ; dd MMMM yyyy");
                dateInfo.setText(outputFormat.format(new Date()));
            }
        }
        catch(Exception e) { }
    }

    @Override
    public void run() {
        setTheDateAndTime();
        updateTick.postDelayed(this, updateDelay);
    }
}

package project.mssd2.mssd;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by judeeofficial on 28/4/2559.
 */
public class start_activity extends FragmentActivity {
    Handler handler = new Handler();
    Timer timer = new Timer();
    TimerTask timetask;

    private final long startTime = 10000;
    private final long interval = 1000;
    MyCountDown countdown;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        countdown = new MyCountDown(startTime,interval);
        countdown.start();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public class MyCountDown extends CountDownTimer {
        public MyCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
            Intent it = new Intent(getApplicationContext(),loginActivity.class);
            startActivity(it);
        }

        @Override
        public void onTick(long remain) {
            // TODO Auto-generated method stub

        }

    }

}
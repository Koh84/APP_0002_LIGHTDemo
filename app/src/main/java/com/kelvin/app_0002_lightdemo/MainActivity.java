package com.kelvin.app_0002_lightdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends AppCompatActivity {

    private Button mLightButton = null;
    boolean flashing = false;
    final private int LED_NOTIFICATION_ID = 123;
    private Handler mLightHandler = new Handler();
    private LightRunnable mLightRunnable = new LightRunnable();

    private SeekBar mBacklightSeekBar = null;

    class LightRunnable implements Runnable{
        @Override
        public void run() {
            if(flashing){
                FlashingLight();
            }else
            {
                ClearLED();
            }
        }
    }

    private void FlashingLight()
    {
        NotificationManager nm = ( NotificationManager ) getSystemService( NOTIFICATION_SERVICE );
        Notification notif = new Notification();

        notif.ledARGB = 0xFF0000ff;
        notif.flags = Notification.FLAG_SHOW_LIGHTS;
        notif.ledOnMS = 100;
        notif.ledOffMS = 100;
        nm.notify(LED_NOTIFICATION_ID, notif);
    }

    private void ClearLED()
    {
        NotificationManager nm = ( NotificationManager ) getSystemService( NOTIFICATION_SERVICE );
        nm.cancel( LED_NOTIFICATION_ID );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLightButton = (Button) findViewById(R.id.button);
        mLightButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                flashing = !flashing;

                if (flashing) {
                    mLightButton.setText("Stop Flashing Light");
                } else {
                    mLightButton.setText("Flashing Light at 20s");
                }

                mLightHandler.postDelayed(mLightRunnable, 20000);

            }
        });

        mBacklightSeekBar = (SeekBar) findViewById(R.id.seekBar);

        try {
            Settings.System.putInt(getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

            int brightness = android.provider.Settings.System.getInt(getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS);

            mBacklightSeekBar.setProgress(brightness/255*100);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        mBacklightSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int brightness = mBacklightSeekBar.getProgress();
                brightness = brightness *255/100;

                android.provider.Settings.System.putInt(getContentResolver(),
                        android.provider.Settings.System.SCREEN_BRIGHTNESS,
                        brightness);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}

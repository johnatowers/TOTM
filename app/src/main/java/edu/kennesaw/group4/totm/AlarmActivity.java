package edu.kennesaw.group4.totm;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import static java.security.AccessController.getContext;

public class AlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        Intent incoming = getIntent();
        String name = incoming.getStringExtra("name");
        String time = incoming.getStringExtra("time");
        String where = incoming.getStringExtra("where");
        ((TextView) findViewById(R.id.name)).setText(name);
        ((TextView) findViewById(R.id.time)).setText(time);
        ((TextView) findViewById(R.id.where)).setText(where);
        MediaPlayer mediaPlayerScan = new MediaPlayer();
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        try {
            mediaPlayerScan.setDataSource(this,
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));

            mediaPlayerScan.setAudioAttributes(new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build());
            mediaPlayerScan.setLooping(true);
            mediaPlayerScan.prepare();
            mediaPlayerScan.start();
            v.vibrate(VibrationEffect.createWaveform(new long[]{3000},-1));
        } catch (IOException e) {
            e.printStackTrace();
        }
        findViewById(R.id.dissmissButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayerScan.stop();
                finish();
            }
        });
    }

}

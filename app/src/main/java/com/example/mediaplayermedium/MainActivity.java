package com.example.mediaplayermedium;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {

    MediaPlayer player;
    private TextView TXTtime, TXTcurrentTime;
    private long currentTime, time;
    private boolean isPlaying;
    private SeekBar music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TXTtime = (TextView) findViewById(R.id.time);
        TXTcurrentTime = (TextView) findViewById(R.id.currentTime);

        music = (SeekBar) findViewById(R.id.timeMusic);
    }

    public void play(View v){
        if(player==null){
            Toast.makeText(this, "Sem música selecionada", Toast.LENGTH_SHORT).show();
        }else{
            player.setOnPreparedListener(this);
            player.start();
            isPlaying = true;
            updateTimeMusicThread(player, TXTcurrentTime, TXTtime);
        }
    }

    public void pause(View v){
        isPlaying = false;
        if(player!=null){
            player.pause();
            /*time = player.getDuration();
            currentTime = player.getCurrentPosition();*/
        }
    }

    public void stop(View v){
        stopPlayer();
    }

    private void stopPlayer(){
        isPlaying = false;
        if(player!=null){
            player.release();
            player=null;
            TXTcurrentTime.setText("");
            TXTtime.setText("");
            music.setProgress(0);
            music.setMax(0);
            Toast.makeText(this, "MediaPlayer liberado", Toast.LENGTH_SHORT).show();
        }
    }

    private void tradeSong(){
        isPlaying = false;
        player.release();
        player=null;
        TXTcurrentTime.setText("00:00");
        TXTtime.setText("00:00");
        music.setProgress(0);
        music.setMax(0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }

    public void song1(View v){
        if(player!=null){
            tradeSong();
        }
        player = MediaPlayer.create(this, R.raw.song1);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlayer();
            }
        });
    }

    public void song2(View v){
        if(player!=null){
            tradeSong();
        }
        player = MediaPlayer.create(this, R.raw.song2);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlayer();
            }
        });
    }

    public void song3(View v){
        if(player!=null){
            tradeSong();
        }
        player = MediaPlayer.create(this, R.raw.song3);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlayer();
            }
        });
    }

    //Alterar tempo da musica
    public void updateTimeMusic(final long duration, final long currentTime, final TextView TXTcurrentTime, final TextView TXTtime){
        runOnUiThread(new Runnable(){
            public void run(){
                long aux;
                int minute, second;

                // DURATION
                aux = duration / 1000;
                minute = (int) (aux / 60);
                second = (int) (aux % 60);
                int musicDuration = (int)duration;
                String sDuration = minute < 10 ? "0"+minute : minute+"";
                sDuration += ":"+(second < 10 ? "0"+second : second);

                // CURRENTTIME
                aux = currentTime / 1000;
                minute = (int) (aux / 60);
                second = (int) (aux % 60);
                int musicCurrentTime = (int)currentTime;
                String sCurrentTime = minute < 10 ? "0"+minute : minute+"";
                sCurrentTime += ":"+(second < 10 ? "0"+second : second);

                TXTcurrentTime.setText(sCurrentTime);
                music.setProgress(musicCurrentTime);
                TXTtime.setText(sDuration);
                music.setMax(musicDuration);
            }
        });
    }

    //Thread para alterar tempo da música
    public void updateTimeMusicThread(final MediaPlayer mp, final TextView TXTcurrentTime, final TextView TXTtime){
        new Thread(){
            public void run(){
                while(isPlaying){
                    try{
                        updateTimeMusic(mp.getDuration(), mp.getCurrentPosition(), TXTcurrentTime, TXTtime);
                        Thread.sleep(1000);
                    }
                    catch(IllegalStateException e){ e.printStackTrace(); }
                    catch (InterruptedException e) { e.printStackTrace(); }
                }
            }
        }.start();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isPlaying = true;

        mp.start();
        mp.seekTo((int)currentTime);

        updateTimeMusicThread(mp, TXTcurrentTime, TXTtime);
    }
}
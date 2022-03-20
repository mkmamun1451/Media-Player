package com.example.mymediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

public class PlaySongs extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();
    }

    private ImageView playIV, prcviousIV, nextIV;
    private TextView textV;
    private ArrayList<File> songs;
    private MediaPlayer mediaPlayer;
    private String textContent;
    private int position;
    private SeekBar seekBar;
    private Thread updateSeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_songs);

        textV = findViewById(R.id.textVw);
        playIV = findViewById(R.id.playIv);
        prcviousIV = findViewById(R.id.prcviousIv);
        nextIV = findViewById(R.id.nextIv);
        seekBar = findViewById(R.id.seekBar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("songsList");
        textContent = intent.getStringExtra("currentSongs");
        textV.setText(textContent);
        textV.setSelected(true);
        position = intent.getIntExtra("position",0);
        Uri url = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this, url);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        updateSeek = new Thread(){
            @Override
            public void run() {
                int currentPosition = 0;
                try {
                    while (currentPosition<mediaPlayer.getDuration()){
                        currentPosition = mediaPlayer.getCurrentPosition();
                        sleep(800);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateSeek.start();

        playIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()){
                    playIV.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                }else {
                    playIV.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                }
            }
        });

         prcviousIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (position!=0){
                    position = position - 1;
                }else {
                    position = songs.size() - 1;
                }
                Uri url = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), url);
                mediaPlayer.start();
                playIV.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());
                textContent = songs.get(position).getName().toString();
                textV.setText(textContent);
            }
        });


         nextIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (position!=songs.size() - 1){
                    position = position + 1;
                }else {
                    position = 0;
                }
                Uri url = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), url);
                mediaPlayer.start();
                playIV.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());
                textContent = songs.get(position).getName().toString();
                textV.setText(textContent);
            }
        });



    }
}
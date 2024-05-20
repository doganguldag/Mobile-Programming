package com.example.karaokeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.Manifest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class KaraokeActivity extends AppCompatActivity {

    public MediaRecorder recorder;
    public Song selectedSong;
    public TextView lyrics;
    public ArrayList<String> lyricsList;
    public Integer lyricsCounter = 0;
    public String filePath = MainActivity.filePath;
    private static final String[] RECORD_AUDIO_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karaoke);

        selectedSong = (Song) getIntent().getSerializableExtra("selectedSong");

        Button recordButton = findViewById(R.id.karaoke_record_button);
        recordButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String startStr = getResources().getString(R.string.recorder_start);
                String stopStr = getResources().getString(R.string.recorder_stop);

                if (startStr.equals(recordButton.getText().toString())) {

                    if (!arePermissionsGranted()) {

                        ActivityCompat.requestPermissions(KaraokeActivity.this, new String[] {android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                    } else {

                        startRecording();
                        recordButton.setText(stopStr);

                    }

                }
                else {

                    stopRecording();
                    recordButton.setText(startStr);
                    Intent intent = new Intent(KaraokeActivity.this, MainActivity.class);
                    startActivity(intent);

                }

            }

        });

        TextView headerText = findViewById(R.id.karaoke_song_name);
        TextView subHeaderText = findViewById(R.id.karaoke_artist_name);

        headerText.setText(selectedSong.getHeader());
        subHeaderText.setText(selectedSong.getSubHeader());

        lyricsList = selectedSong.getLyrics();
        lyrics = findViewById(R.id.karaoke_lyrics);
        lyrics.setText("Sozleri ilerletmek icin buraya bas!");
        lyrics.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                lyricsStep();

            }

        });

    }

    private void lyricsStep() {


        if (lyricsCounter < lyricsList.size()) {

            lyrics.setText(lyricsList.get(lyricsCounter));
            lyricsCounter++;

        }

    }

    private void startRecording() {

        recorder = new MediaRecorder();

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        String strippedSongName = selectedSong.getHeader().replaceAll("\\s+","");
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);
        Log.d("File:", filePath + strippedSongName + "-" + formattedDate + ".3gp");
        recorder.setOutputFile(filePath + strippedSongName + "-" + formattedDate + ".3gp");

        try {

            recorder.prepare();
            recorder.start();

        }
        catch (IllegalStateException | IOException e) {

            e.printStackTrace();

        }

    }

    private void stopRecording() {

        if (recorder != null) {

            try {

                recorder.stop();

            } catch (IllegalStateException e) {

                e.printStackTrace();

            } finally {

                recorder.reset();
                recorder.release();
                recorder = null;

            }
        }

    }

    private boolean arePermissionsGranted() {

        for (String permission : RECORD_AUDIO_PERMISSIONS) {

            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {

                return false;

            }

        }

        return true;

    }

}
package com.example.seekbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SeekBar seekBar1, seekBar2;
    private TextView textView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar1 = findViewById(R.id.seekBar1);
        seekBar2 = findViewById(R.id.seekBar2);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateRandomNumberAndProceed();
            }
        });
    }

    private void generateRandomNumberAndProceed() {
        Random random = new Random();
        int min = seekBar1.getProgress();
        int max = seekBar2.getProgress();

        int randomNumber = random.nextInt((max - min) + 1) + min;
        textView.setText(String.valueOf(randomNumber));

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                goToSecondActivity(randomNumber);
            }
        }, 2000);
    }

    private void goToSecondActivity(int randomNumber) {
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("randomNumber", randomNumber);
        startActivity(intent);
    }
}







package com.example.seekbar;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class SecondActivity extends AppCompatActivity {

    private LinearLayout layout;
    private int targetColor;
    private int interval = 2000; // Renk değişim aralığı (ms)
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        layout = findViewById(R.id.layout);

        // MainActivity'den gelen rastgele sayıyı al
        int randomNumber = getIntent().getIntExtra("randomNumber", 0);

        // Renk değişim sayısını belirle
        int changeCount = randomNumber;

        // Arka plan rengi için hedef rengi oluştur
        targetColor = generateRandomColor(randomNumber);

        // Arka plan rengini belirli aralıklarla değiştir
        startColorChange(changeCount);
    }

    private int generateRandomColor(int randomNumber) {
        Random random = new Random();
        // Rastgele renk bileşenleri oluştur
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        // Rastgele renk ve rastgele sayıyı kullanarak hedef rengi oluştur
        return Color.rgb((red + randomNumber) % 256, (green + randomNumber) % 256, (blue + randomNumber) % 256);
    }

    private void startColorChange(int changeCount) {
        for (int i = 0; i < changeCount; i++) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Arka plan rengini değiştir
                    layout.setBackgroundColor(targetColor);

                    // Yeniden hedef rengi oluştur
                    targetColor = generateRandomColor(targetColor);
                }
            }, i * interval);
        }
    }
}







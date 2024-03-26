package com.example.currencyapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView tvTRY, tvEUR, tvCHF, tvJPY, tvCAD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTRY = findViewById(R.id.tvTRY);
        tvEUR = findViewById(R.id.tvEUR);
        tvCHF = findViewById(R.id.tvCHF);
        tvJPY = findViewById(R.id.tvJPY);
        tvCAD = findViewById(R.id.tvCAD);
    }

    public void getRates(View view) {
        String accessKey = "9qMq7OElg9rp4lrY4afJm899Z9gh6keh";
        String baseUrl = "https://api.apilayer.com/fixer/latest?symbols=TRY,EUR,CHF,JPY,CAD&base=USD";

        new FetchRatesTask().execute(baseUrl, accessKey);
    }

    private class FetchRatesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            StringBuilder result = new StringBuilder();
            HttpURLConnection connection = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();

                // accessKey'i headers'a ekle
                connection.setRequestProperty("apikey", params[1]);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
            } catch (IOException e) {
                Log.e("MainActivity", "Error fetching rates: " + e.getMessage());
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (s != null && !s.isEmpty()) { // JSON verisi boş değilse devam edin
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.has("rates")) { // "rates" anahtarına sahipse devam edin
                        JSONObject ratesObject = jsonObject.getJSONObject("rates");

                        // Kurları alırken kontrol et
                        if (ratesObject.has("TRY")) {
                            double tryRate = ratesObject.getDouble("TRY");
                            tvTRY.setText("TRY: " + tryRate);
                        }
                        if (ratesObject.has("EUR")) {
                            double eurRate = ratesObject.getDouble("EUR");
                            tvEUR.setText("EUR: " + eurRate);
                        }
                        if (ratesObject.has("CHF")) {
                            double chfRate = ratesObject.getDouble("CHF");
                            tvCHF.setText("CHF: " + chfRate);
                        }
                        if (ratesObject.has("JPY")) {
                            double jpyRate = ratesObject.getDouble("JPY");
                            tvJPY.setText("JPY: " + jpyRate);
                        }
                        if (ratesObject.has("CAD")) {
                            double cadRate = ratesObject.getDouble("CAD");
                            tvCAD.setText("CAD: " + cadRate);
                        }
                    } else {
                        Log.e("MainActivity", "Error: 'rates' key not found in JSON response");
                    }
                } else {
                    Log.e("MainActivity", "Error: Empty JSON response");
                }
            } catch (JSONException e) {
                Log.e("MainActivity", "Error parsing JSON: " + e.getMessage());
                e.printStackTrace();
            }
        }

    }
}
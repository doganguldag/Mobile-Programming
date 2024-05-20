package com.example.karaokeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public RecyclerView songRecyclerView;
    public CustomAdapter customAdapter;
    public static File karaokesDir;
    public static String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        karaokesDir = new File(getFilesDir().getPath() + File.separator + "karaokes");
        karaokesDir.mkdir();
        filePath = karaokesDir.getPath() + File.separator;

        drawerLayout = findViewById(R.id.nav_menu_drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        songRecyclerView = (RecyclerView) findViewById(R.id.songRecyclerView);

        listSongs();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        songRecyclerView.setLayoutManager(linearLayoutManager);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {

            return true;

        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.nav_songs) {

            listSongs();

        } if (item.getItemId() == R.id.nav_records) {

            listRecords();

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    public void listSongs() {

        ArrayList<Song> songList;

        try {

            JSONObject songJSON = getSongJSON(this, "songs.json");
            songList = JSONToSong(songJSON);

        } catch (IOException e) {

            throw new RuntimeException(e);

        } catch (JSONException e) {

            throw new RuntimeException(e);

        }

        customAdapter = new CustomAdapter(this, songList);
        songRecyclerView.setAdapter(customAdapter);

    }

    public void listRecords() {

        ArrayList<Record> recordList = new ArrayList<>();

        if (karaokesDir.exists() && karaokesDir.isDirectory()) {

            File[] files = karaokesDir.listFiles();

            if (files != null && files.length != 0) {

                for (File file : files) {

                    if (file.isFile()) {

                        String fileName = file.getName();
                        String filePath = file.getAbsolutePath();

                        Log.d("File", "Name: " + fileName + ", Path: " + filePath);

                        Integer imageId = getResources().getIdentifier("record_default_image", "drawable", getPackageName());
                        Record record = new Record(fileName, "Ses Kaydi", imageId, filePath);
                        recordList.add(record);

                    }

                }

                customAdapter = new CustomAdapter(this, recordList);
                songRecyclerView.setAdapter(customAdapter);

            } else {

                Toast.makeText(MainActivity.this,"Ses kaydi bulunamadi!", Toast.LENGTH_LONG).show();

            }

        }

    }

    public ArrayList<Song> JSONToSong(JSONObject SongJSON) throws JSONException {

        ArrayList<Song> songList = new ArrayList<>();
        JSONArray songArray = SongJSON.getJSONArray("songs");

        for (int i = 0; i < songArray.length(); i++) {

            JSONObject songObject = songArray.getJSONObject(i);
            String name = songObject.getString("name");
            String artist = songObject.getString("artist");
            String tempImageId = songObject.getString("imageId");
            Integer imageId = getResources().getIdentifier(tempImageId, "drawable", getPackageName());
            JSONArray lyricsArray = songObject.getJSONArray("lyrics");
            ArrayList<String> lyrics = new ArrayList<>();

            for (int j = 0; j < lyricsArray.length(); j++) {
                String lyric = lyricsArray.getString(j);
                lyrics.add(lyric);
            }

            Song song = new Song(name, artist, lyrics, imageId);
            songList.add(song);

        }

        return songList;
        
    }

    public JSONObject getSongJSON(Context ctx, String fileName) throws IOException, JSONException {

        InputStream stream = ctx.getAssets().open(fileName);
        int size = stream.available();

        byte[] bytes = new byte[size];
        stream.read(bytes);
        stream.close();

        String songString = new String(bytes);
        return new JSONObject(songString);

    }

}
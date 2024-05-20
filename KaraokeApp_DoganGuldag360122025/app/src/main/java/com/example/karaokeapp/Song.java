package com.example.karaokeapp;

import static android.app.PendingIntent.getActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class Song extends Item {

    private ArrayList lyrics;

    public Song(String header, String subHeader, ArrayList lyrics, Integer imageId) {

        super(header, subHeader, imageId);
        this.lyrics = lyrics;

    }

    public ArrayList getLyrics() {

        return this.lyrics;

    }

}

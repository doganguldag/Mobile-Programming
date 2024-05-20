package com.example.karaokeapp;

import android.os.Environment;

import java.util.ArrayList;

public class Record extends Item {

    public String filePath;

    public Record(String header, String subHeader, Integer imageId, String filePath) {

        super(header, subHeader, imageId);
        this.filePath = filePath;

    }

}

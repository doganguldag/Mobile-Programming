package com.example.karaokeapp;

import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable {

    private String header;
    private String subHeader;
    private Integer imageId;

    public Item(String header, String subHeader, Integer imageId) {

        this.header = header;
        this.subHeader = subHeader;
        this.imageId = imageId;

    }

    public String getHeader() {

        return this.header;

    }

    public String getSubHeader() {

        return this.subHeader;

    }

    public int getImageId() {

        return this.imageId;

    }

}

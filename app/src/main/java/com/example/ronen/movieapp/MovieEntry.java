package com.example.ronen.movieapp;

/**
 * Created by Ronen on 19/11/2016.
 */

public class MovieEntry {
    private String imageUrl;
    private String id;

    public MovieEntry(String image_uri, String id) {
        this.imageUrl = image_uri;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

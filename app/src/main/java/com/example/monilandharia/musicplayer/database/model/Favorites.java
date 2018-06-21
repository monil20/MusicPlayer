package com.example.monilandharia.musicplayer.database.model;

public class Favorites {


    private int songId;


    public Favorites() {
    }

    public Favorites(int songId) {
        this.songId = songId;
    }

    public int getId() {
        return songId;
    }

    public void setId(int songId) {
        this.songId = songId;
    }

}

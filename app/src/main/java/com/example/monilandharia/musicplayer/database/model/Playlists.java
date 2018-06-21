package com.example.monilandharia.musicplayer.database.model;

public class Playlists {

    private int playlistId;
    private String title;

    public Playlists() {
    }

    public Playlists(int playlistId, String title) {
        this.playlistId = playlistId;
        this.title = title;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

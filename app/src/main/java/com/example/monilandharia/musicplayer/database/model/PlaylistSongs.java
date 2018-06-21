package com.example.monilandharia.musicplayer.database.model;

public class PlaylistSongs {

    private int playlistId;
    private int songId;

    public PlaylistSongs() {
    }

    public PlaylistSongs(int playlistId, int songId) {
        this.playlistId = playlistId;
        this.songId = songId;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }
}

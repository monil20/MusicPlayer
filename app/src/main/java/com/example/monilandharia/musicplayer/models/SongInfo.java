package com.example.monilandharia.musicplayer.models;

public class SongInfo {
    private String song_name, song_artist, data, album_name;
    private int song_id, album_id, song_duration;

    public SongInfo(int song_id, String song_name, String song_artist, int song_duration, int album_id, String data, String album_name) {
        this.song_id = song_id;
        this.song_name = song_name;
        this.song_artist = song_artist;
        this.song_duration = song_duration;
        this.album_id = album_id;
        this.data = data;
        this.album_name = album_name;
    }

    public int getSong_id() {
        return song_id;
    }

    public void setSong_id(int song_id) {
        this.song_id = song_id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public String getSong_name() {
        return song_name;
    }

    public void setSong_name(String song_name) {
        this.song_name = song_name;
    }

    public String getSong_artist() {
        return song_artist;
    }

    public void setSong_artist(String song_artist) {
        this.song_artist = song_artist;
    }

    public int getSong_duration() {
        return song_duration;
    }

    public void setSong_duration(int song_duration) {
        this.song_duration = song_duration;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }
}

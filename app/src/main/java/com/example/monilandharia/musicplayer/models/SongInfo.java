package com.example.monilandharia.musicplayer.models;

public class SongInfo {
    private String song_name;
    private String song_artist;
    private int song_duration;
    private long album_id;
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public SongInfo(String song_name, String song_artist, int song_duration, long album_id, String data) {

        this.song_name = song_name;
        this.song_artist = song_artist;
        this.song_duration = song_duration;
        this.album_id = album_id;
        this.data = data;
    }

    public long getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(long album_id) {
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
}

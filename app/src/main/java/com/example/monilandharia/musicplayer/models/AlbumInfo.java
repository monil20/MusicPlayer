package com.example.monilandharia.musicplayer.models;

public class AlbumInfo {
    private final long artistId;
    private String artistName;
    private final long id;
    private final int songCount;
    private final String title;
    private final int year;

    public AlbumInfo() {
        this.id = -1;
        this.title = "";
        this.artistName = "";
        this.artistId = -1;
        this.songCount = -1;
        this.year = -1;
    }

    public AlbumInfo(long id, String title, String artistName, long artistId, int songCount, int year) {
        this.id = id;
        this.title = title;
        this.artistName = artistName;
        this.artistId = artistId;
        this.songCount = songCount;
        this.year = year;
    }

    public long getArtistId() {
        return artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public long getId() {
        return id;
    }

    public int getSongCount() {
        return songCount;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }
}
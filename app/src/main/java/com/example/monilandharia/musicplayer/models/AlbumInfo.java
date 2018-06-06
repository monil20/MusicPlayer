package com.example.monilandharia.musicplayer.models;

import java.util.ArrayList;

public class AlbumInfo {
//    private long albumId;
    private String album;
    private String albumArtist;
    private String albumKey;

    private String albumArt;

    private int albumSongsCount;
    public AlbumInfo(String albumArt, String album, String albumArtist, String albumKey, int albumSongsCount) {
        this.albumArt = albumArt;
//        this.albumId = albumId;
        this.album = album;
        this.albumArtist = albumArtist;
        this.albumKey = albumKey;
        this.albumSongsCount = albumSongsCount;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

//    public int getAlbumId() {
//        return albumId;
//    }
//
//    public void setAlbumId(int albumId) {
//        this.albumId = albumId;
//    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumArtist() {
        return albumArtist;
    }

    public void setAlbumArtist(String albumArtist) {
        this.albumArtist = albumArtist;
    }

    public String getAlbumKey() {
        return albumKey;
    }

    public void setAlbumKey(String albumKey) {
        this.albumKey = albumKey;
    }

    public int getAlbumSongsCount() {
        return albumSongsCount;
    }

    public void setAlbumSongsCount(int albumSongsCount) {
        this.albumSongsCount = albumSongsCount;
    }
}

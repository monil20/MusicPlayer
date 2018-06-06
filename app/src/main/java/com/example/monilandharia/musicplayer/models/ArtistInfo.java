package com.example.monilandharia.musicplayer.models;

public class ArtistInfo {
    private int _id, albumsCount, tracksCount;
    private String artistName, artistKey;

    public ArtistInfo(int _id, int albumsCount, int tracksCount, String artistName, String artistKey) {
        this._id = _id;
        this.albumsCount = albumsCount;
        this.tracksCount = tracksCount;
        this.artistName = artistName;
        this.artistKey = artistKey;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getAlbumsCount() {
        return albumsCount;
    }

    public void setAlbumsCount(int albumsCount) {
        this.albumsCount = albumsCount;
    }

    public int getTracksCount() {
        return tracksCount;
    }

    public void setTracksCount(int tracksCount) {
        this.tracksCount = tracksCount;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistKey() {
        return artistKey;
    }

    public void setArtistKey(String artistKey) {
        this.artistKey = artistKey;
    }
}

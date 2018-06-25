package com.example.monilandharia.musicplayer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.monilandharia.musicplayer.MainActivity;
import com.example.monilandharia.musicplayer.database.model.PlaylistSongs;
import com.example.monilandharia.musicplayer.database.model.Playlists;
import com.example.monilandharia.musicplayer.models.SongInfo;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Log String
    private static final String LOG = "XXX";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "musicplayer_db";

    // Table Names
    private static final String TABLE_FAVORITES = "favorites";
    private static final String TABLE_PLAYLISTS = "playlists";
    private static final String TABLE_PLAYLIST_SONGS = "playlistSongs";

    // Columns
    private static final String COLUMN_SONG_ID = "songId";
    private static final String COLUMN_PLAYLIST_ID = "playlistId";
    private static final String COLUMN_PLAYLIST_TITLE = "title";

    // Creating Tables
    private static final String CREATE_TABLE_FAVORITES =
            "CREATE TABLE " + TABLE_FAVORITES + "("
                    + COLUMN_SONG_ID + " INTEGER PRIMARY KEY"
                    + ")";

    public static final String CREATE_TABLE_PLAYLISTS =
            "CREATE TABLE " + TABLE_PLAYLISTS + "("
                    + COLUMN_PLAYLIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_PLAYLIST_TITLE + " TEXT"
                    + ")";

    public static final String CREATE_TABLE_PLAYLIST_SONGS =
            "CREATE TABLE " + TABLE_PLAYLIST_SONGS + "("
                    + COLUMN_PLAYLIST_ID + " INTEGER,"
                    + COLUMN_SONG_ID + " INTEGER"
                    + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // create tables
        db.execSQL(CREATE_TABLE_FAVORITES);
        db.execSQL(CREATE_TABLE_PLAYLISTS);
        db.execSQL(CREATE_TABLE_PLAYLIST_SONGS);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLISTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLIST_SONGS);

        // Create tables again
        onCreate(db);
    }

    // Favorites CRUD
    public long addFav(int songId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SONG_ID, songId);

        // insert row
        long result = db.insert(TABLE_FAVORITES, null, values);

        return result;
    }

    public ArrayList<Integer> getFavs() {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_FAVORITES;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        ArrayList<Integer> favs = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                favs.add(c.getInt(c.getColumnIndex(COLUMN_SONG_ID)));
            } while (c.moveToNext());
        }

        return favs;
    }

    public void removeFav(int songId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, COLUMN_SONG_ID + " = ?",
                new String[]{String.valueOf(songId)});
    }

    // Playlist CRUD

    public ArrayList<Playlists> getPlaylists() {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_PLAYLISTS;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        ArrayList<Playlists> playlists = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                playlists.add(new Playlists(c.getInt(c.getColumnIndex(COLUMN_PLAYLIST_ID)), c.getString(c.getColumnIndex(COLUMN_PLAYLIST_TITLE))));
            } while (c.moveToNext());
        }

        return playlists;
    }

    public int addPlaylist(String title) {
        SQLiteDatabase dbW = this.getWritableDatabase();
        SQLiteDatabase dbR = this.getReadableDatabase();
        boolean newEntry = true;
        int result = -1;

        String selectQuery = "SELECT  * FROM " + TABLE_PLAYLISTS;

        Log.e(LOG, selectQuery);

        // Check for existing title
        Cursor c = dbR.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                if (title.equals(c.getString(c.getColumnIndex(COLUMN_PLAYLIST_TITLE)))) {
                    newEntry = false;
                }
            } while (c.moveToNext());
        }

        // Make a new playlist
        if (newEntry) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_PLAYLIST_TITLE, title);

            // insert row
            dbW.insert(TABLE_PLAYLISTS, null, values);

            // Fetching playlist ID of recently added playlist
            c = dbR.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                do {
                    if (title.equals(c.getString(c.getColumnIndex(COLUMN_PLAYLIST_TITLE)))) {
                        result = c.getInt(c.getColumnIndex(COLUMN_PLAYLIST_ID));
                        break;
                    }
                } while (c.moveToNext());
            }
        }
        return result;
    }

    public ArrayList<SongInfo> getSongsForPlaylist(int playlistId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT " + COLUMN_SONG_ID + " FROM " + TABLE_PLAYLIST_SONGS + " WHERE " + COLUMN_PLAYLIST_ID + " = "+playlistId;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        ArrayList<Integer> playlistSongIds = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                playlistSongIds.add(c.getInt(c.getColumnIndex(COLUMN_SONG_ID)));
            } while (c.moveToNext());
            Log.e("XXX",playlistSongIds.size()+"");
        }

        ArrayList<SongInfo> playlistSongs = new ArrayList<>();

        for (int j = 0; j < playlistSongIds.size(); j++) {
            for (int i = 0; i < MainActivity.songs.size(); i++) {
                if(playlistSongIds.get(j) == MainActivity.songs.get(i).getSong_id()){
                    playlistSongs.add(MainActivity.songs.get(i));
                    break;
                }
            }
        }

        return playlistSongs;
    }

    public long addSongToPlaylist(String title, int songId) {
        SQLiteDatabase dbW = this.getWritableDatabase();
        SQLiteDatabase dbR = this.getReadableDatabase();
        int playlistId = 0;

        String selectQuery = "SELECT  * FROM " + TABLE_PLAYLISTS;

        Log.e(LOG, selectQuery);

        // Check for existing title
        Cursor c = dbR.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                if (title.equals(c.getString(c.getColumnIndex(COLUMN_PLAYLIST_TITLE)))) {
                    playlistId = c.getInt(c.getColumnIndex(COLUMN_PLAYLIST_ID));
                    break;
                }
            } while (c.moveToNext());
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_SONG_ID, songId);
        values.put(COLUMN_PLAYLIST_ID, playlistId);

        // insert row
        long result = dbW.insert(TABLE_PLAYLIST_SONGS, null, values);

        return result;
    }


    public void removePlaylist(String title) {
        SQLiteDatabase dbW = this.getWritableDatabase();
        SQLiteDatabase dbR = this.getReadableDatabase();
        int playlistId = 0;

        String selectQuery = "SELECT  * FROM " + TABLE_PLAYLISTS;

        Log.e(LOG, selectQuery);

        // Check for existing title
        Cursor c = dbR.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                if (title.equals(c.getString(c.getColumnIndex(COLUMN_PLAYLIST_TITLE)))) {
                    playlistId = c.getInt(c.getColumnIndex(COLUMN_PLAYLIST_ID));
                    break;
                }
            } while (c.moveToNext());
        }

        dbW.delete(TABLE_PLAYLIST_SONGS, COLUMN_PLAYLIST_ID + " = ?",
                new String[]{String.valueOf(playlistId)});
        dbW.delete(TABLE_PLAYLISTS, COLUMN_PLAYLIST_ID + " = ?",
                new String[]{String.valueOf(playlistId)});
    }

    public void removeSongFromPlaylist(int sondId, String title) {
        SQLiteDatabase dbW = this.getWritableDatabase();
        SQLiteDatabase dbR = this.getReadableDatabase();
        int playlistId = 0;

        String selectQuery = "SELECT  * FROM " + TABLE_PLAYLISTS;

        Log.e(LOG, selectQuery);

        // Check for existing title
        Cursor c = dbR.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
//                Log.i("HERE", title+"XXX"+c.getString(c.getColumnIndex(COLUMN_PLAYLIST_TITLE)));
                if (title.equals(c.getString(c.getColumnIndex(COLUMN_PLAYLIST_TITLE)))) {
                    playlistId = c.getInt(c.getColumnIndex(COLUMN_PLAYLIST_ID));
                    break;
                }
            } while (c.moveToNext());
        }

//        Log.i("HERE", playlistId+" "+sondId);
        dbW.delete(TABLE_PLAYLIST_SONGS, COLUMN_PLAYLIST_ID + " = ? AND " + COLUMN_SONG_ID + " = ?",
                new String[]{String.valueOf(playlistId), String.valueOf(sondId)});
    }

    // Close DB
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
package com.example.monilandharia.musicplayer.dataLoaders;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.monilandharia.musicplayer.models.SongInfo;

import java.util.ArrayList;

public class ArtistSongLoader {
    public static ArrayList<SongInfo> getSongsForArtist(Context context, long artistID) {
        Cursor cursor = makeArtistSongCursor(context, artistID);
        ArrayList songsList = new ArrayList();
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String artist = cursor.getString(2);
                int duration = cursor.getInt(3);
                int albumId = cursor.getInt(4);
                String data = cursor.getString(5);
                String album = cursor.getString(6);
//                while (trackNumber >= 1000) {
//                    trackNumber -= 1000; //When error occurs the track numbers have an extra 1000 or 2000 added, so decrease till normal.
//                }
                songsList.add(new SongInfo(id, title, artist, duration, albumId, data, album));
            }
            while (cursor.moveToNext());
        if (cursor != null)
            cursor.close();
        return songsList;
    }

    public static Cursor makeArtistSongCursor(Context context, long artistID) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] trackProjection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM,
        };
        String string = "is_music=1 AND title != '' AND artist_id=" + artistID;
        return contentResolver.query(uri,trackProjection, string, null, MediaStore.Audio.Media.TITLE);
    }
}

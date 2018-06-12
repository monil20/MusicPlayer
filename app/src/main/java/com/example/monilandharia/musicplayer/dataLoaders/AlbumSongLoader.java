package com.example.monilandharia.musicplayer.dataLoaders;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.monilandharia.musicplayer.models.SongInfo;

import java.util.ArrayList;

public class AlbumSongLoader {

    public static ArrayList<SongInfo> getSongsForAlbum(Context context, long albumID) {

        Cursor cursor = makeAlbumSongCursor(context, albumID);
        ArrayList arrayList = new ArrayList();
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String artist = cursor.getString(2);
                int duration = cursor.getInt(3);
                String data = cursor.getString(5);
                String album = cursor.getString(6);
//                while (trackNumber >= 1000) {
//                    trackNumber -= 1000; //When error occurs the track numbers have an extra 1000 or 2000 added, so decrease till normal.
//                }
                long artistId = cursor.getInt(6);
                int albumId =(int) albumID;
                arrayList.add(new SongInfo(id, title, artist, duration, albumId, data, album));
            }
            while (cursor.moveToNext());
        if (cursor != null)
            cursor.close();
        return arrayList;
    }

    public static Cursor makeAlbumSongCursor(Context context, long albumID) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String string = "is_music=1 AND title != '' AND album_id=" + albumID;
        String[] trackProjection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM,
        };
        Cursor cursor = contentResolver.query(uri, trackProjection, string, null, MediaStore.Audio.Media.TITLE);
        return cursor;
    }
}

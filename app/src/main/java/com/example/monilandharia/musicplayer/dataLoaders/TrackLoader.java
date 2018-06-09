package com.example.monilandharia.musicplayer.dataLoaders;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.monilandharia.musicplayer.models.AlbumInfo;
import com.example.monilandharia.musicplayer.models.SongInfo;

import java.util.ArrayList;

public class TrackLoader {
    public static ArrayList<SongInfo> getTracksForCursor(Cursor cursor,int i) {
        ArrayList arrayList = new ArrayList();
        int n=0;
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                arrayList.add(new SongInfo(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6)));
                n++;
            }
            while (cursor.moveToNext()&&n<i);
        if (cursor != null)
            cursor.close();
        return arrayList;
    }

    public static ArrayList<SongInfo> getTracksForCursor(Cursor cursor) {
        ArrayList arrayList = new ArrayList();
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                arrayList.add(new SongInfo(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6)));
            }
            while (cursor.moveToNext());
        if (cursor != null)
            cursor.close();
        return arrayList;
    }

    public static ArrayList<SongInfo> getAllTracks(Context context) {
        return getTracksForCursor(makeTrackCursor(context, null, null));
    }

    public static ArrayList<SongInfo> getAllTracks(Context context,int n) {
        return getTracksForCursor(makeTrackCursor(context, null, null),n);
    }

    public static Cursor makeTrackCursor(Context context, String selection, String[] paramArrayOfString) {
        String[] trackProjection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM,
        };
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, trackProjection, selection, paramArrayOfString, MediaStore.Audio.Media.TITLE);

        return cursor;
    }
}

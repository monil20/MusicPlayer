package com.example.monilandharia.musicplayer.dataLoaders;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.monilandharia.musicplayer.models.AlbumInfo;

import java.util.ArrayList;

public class AlbumLoader {

    public static ArrayList<AlbumInfo> getAlbumsForCursor(Cursor cursor,int n) {
        ArrayList arrayList = new ArrayList();
        int i=0;
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                arrayList.add(new AlbumInfo(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getLong(3), cursor.getInt(4), cursor.getInt(5)));
                i++;
            }
            while (cursor.moveToNext()&&i<n);
        if (cursor != null)
            cursor.close();
        return arrayList;
    }

    public static ArrayList<AlbumInfo> getAllAlbums(Context context,int n) {
        return getAlbumsForCursor(makeAlbumCursor(context, null, null),n);
    }

    //Methods without any contraints
    public static ArrayList<AlbumInfo> getAlbumsForCursor(Cursor cursor) {
        ArrayList arrayList = new ArrayList();
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                arrayList.add(new AlbumInfo(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getLong(3), cursor.getInt(4), cursor.getInt(5)));
            }
            while (cursor.moveToNext());
        if (cursor != null)
            cursor.close();
        return arrayList;
    }

    public static ArrayList<AlbumInfo> getAllAlbums(Context context) {
        return getAlbumsForCursor(makeAlbumCursor(context, null, null));
    }

    public static Cursor makeAlbumCursor(Context context, String selection, String[] paramArrayOfString) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]{"_id", "album", "artist", "artist_id", "numsongs", "minyear"}, selection, paramArrayOfString, "album");

        return cursor;
    }
}

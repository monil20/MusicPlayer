package com.example.monilandharia.musicplayer.dataLoaders;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.monilandharia.musicplayer.models.AlbumInfo;

import java.util.ArrayList;

public class ArtistAlbumLoader {
    public static ArrayList<AlbumInfo> getAlbumsForArtist(Context context, long artistID) {

        ArrayList albumList = new ArrayList();
        Cursor cursor = makeAlbumForArtistCursor(context, artistID);

        if (cursor != null) {
            if (cursor.moveToFirst())
                do {

                    albumList.add(new AlbumInfo(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getLong(3), cursor.getInt(4), cursor.getInt(5)));
                }
                while (cursor.moveToNext());

        }
        if (cursor != null)
            cursor.close();
        return albumList;
    }

    public static Cursor makeAlbumForArtistCursor(Context context, long artistID) {

        if (artistID == -1)
            return null;

        return context.getContentResolver()
                .query(MediaStore.Audio.Artists.Albums.getContentUri("external", artistID),
                        new String[]{"_id", "album", "artist", "artist_id", "numsongs", "minyear"},
                        null,
                        null,
                        "album");
    }
}

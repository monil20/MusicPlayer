package com.example.monilandharia.musicplayer.dataLoaders;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.monilandharia.musicplayer.models.ArtistInfo;
import com.example.monilandharia.musicplayer.models.SongInfo;

import java.util.ArrayList;
import java.util.List;

public class ArtistLoader {
    public static ArrayList<ArtistInfo> getArtistsForCursor(Cursor cursor,Context context,int m) {
        ArrayList arrayList = new ArrayList();
        int n=0;
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                int _id = cursor.getInt(0);

                ArrayList artistSongs = getArtistSongs(_id,context);
                int songArt[] = new int[4];
                for(int i=0;i<4;i++)
                {
                    int index =(int)(Math.random()*artistSongs.size());
                    SongInfo aSong = (SongInfo)artistSongs.get(index);
                    songArt[i] = aSong.getAlbum_id();
                }
                arrayList.add(new ArtistInfo(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4),songArt));
                n++;
            }
            while (cursor.moveToNext()&&n<m);
        if (cursor != null)
            cursor.close();
        return arrayList;
    }

    public static ArrayList<ArtistInfo> getAllArtists(Context context,int n) {
        return getArtistsForCursor(makeArtistCursor(context, null, null),context,n);
    }

    //Overrided method to retreive all the artist list
    public static ArrayList<ArtistInfo> getArtistsForCursor(Cursor cursor,Context context) {
        ArrayList arrayList = new ArrayList();
        int n=0;
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                int _id = cursor.getInt(0);

                ArrayList artistSongs = getArtistSongs(_id,context);
                int songArt[] = new int[4];
                for(int i=0;i<4;i++)
                {
                    int index =(int)(Math.random()*artistSongs.size());
                    SongInfo aSong = (SongInfo)artistSongs.get(index);
                    songArt[i] = aSong.getAlbum_id();
                }
                arrayList.add(new ArtistInfo(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4),songArt));
                n++;
            }
            while (cursor.moveToNext()&&n<6);
        if (cursor != null)
            cursor.close();
        return arrayList;
    }

    public static ArrayList<ArtistInfo> getAllArtists(Context context) {
        return getArtistsForCursor(makeArtistCursor(context, null, null),context);
    }

    public static Cursor makeArtistCursor(Context context, String selection, String[] paramArrayOfString) {
        String[] artistProjection = {
                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
                MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.ARTIST_KEY
        };
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, artistProjection, selection, paramArrayOfString,MediaStore.Audio.Artists.ARTIST);
        return cursor;
    }

    private static ArrayList getArtistSongs(int id,Context context) {
        String[] songForArtistProjection = {
                "_id",
                "title",
                "artist",
                "album",
                "duration",
                "track",
                "album_id",
                "_data"
        };
        Cursor songsForArtistCursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                songForArtistProjection,
                "is_music=1 AND title != '' AND artist_id=" + id,
                null,
                null);
        ArrayList songsList = new ArrayList();
        if ((songsForArtistCursor != null) && (songsForArtistCursor.moveToFirst()))
            do {
                int songid = songsForArtistCursor.getInt(0);
                String title = songsForArtistCursor.getString(1);
                String artist = songsForArtistCursor.getString(2);
                String album = songsForArtistCursor.getString(3);
                int duration = songsForArtistCursor.getInt(4);
                int albumId = songsForArtistCursor.getInt(6);
                String data = songsForArtistCursor.getString(7);

                songsList.add(new SongInfo(songid, title, artist, duration, albumId,data,album));
            }
            while (songsForArtistCursor.moveToNext());
        if (songsForArtistCursor != null)
            songsForArtistCursor.close();
        return songsList;
    }
}
